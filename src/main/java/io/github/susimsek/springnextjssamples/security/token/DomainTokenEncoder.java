package io.github.susimsek.springnextjssamples.security.token;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.factories.DefaultJWSSignerFactory;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.produce.JWSSignerFactory;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncodingException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * {@code DomainTokenEncoder} is responsible for encoding JSON Web Tokens (JWTs) with JSON Web Signature (JWS) and
 * optional JSON Web Encryption (JWE) using RSA keys.
 *
 * <p>This class leverages the Nimbus JOSE library to create, sign, and optionally encrypt JWTs. It selects appropriate
 * keys from the provided {@link JWKSource} based on header parameters and constructs signed and encrypted JWTs.</p>
 *
 * <p>Example usage:
 * <pre>
 *     JWKSource<SecurityContext> jwkSource = ...;
 *     DomainTokenEncoder encoder = new DomainTokenEncoder(jwkSource);
 *     Jwt jwt = encoder.encode(parameters, rsaKey);
 * </pre>
 * </p>
 */
@RequiredArgsConstructor
public class DomainTokenEncoder implements TokenEncoder {

    private static final String ENCODING_ERROR_MESSAGE_TEMPLATE =
        "An error occurred while attempting to encode the Jwt: %s";
    private final JWKSource<SecurityContext> jwkSource;
    private static final JWSSignerFactory JWS_SIGNER_FACTORY;
    private final Map<JWK, JWSSigner> jwsSigners = new ConcurrentHashMap<>();

    /**
     * Encodes the JWT with the given parameters, optionally signing and encrypting it.
     *
     * @param parameters the token encoding parameters, including claims and headers
     * @param rsaKey     the RSA key to use for encryption if JWE is applied
     * @return the encoded {@link Jwt} instance
     * @throws JwtEncodingException if an error occurs during encoding
     */
    @Override
    public Jwt encode(TokenEncoderParameters parameters, RSAKey rsaKey) throws JwtEncodingException {
        Assert.notNull(parameters, "parameters cannot be null");

        JwtClaimsSet claims = parameters.claims();
        JwsHeader headers = parameters.jwsHeader();
        if (headers == null) {
            headers = JwsHeader.with(SignatureAlgorithm.RS256).build();
        }

        JWSHeader jwsHeader = convert(headers);
        JWTClaimsSet claimsSet = convert(claims);
        JWK jwk = selectJwk(headers);
        headers = addKeyIdentifierHeadersIfNecessary(headers, jwk);
        SignedJWT signedJwt = new SignedJWT(jwsHeader, claimsSet);
        JWSSigner signer = this.jwsSigners.computeIfAbsent(jwk, DomainTokenEncoder::createSigner);

        try {
            signedJwt.sign(signer);
            if (parameters.jweHeader() == null) {
                return new Jwt(signedJwt.serialize(),
                    claims.getIssuedAt(), claims.getExpiresAt(), headers.getHeaders(), claims.getClaims());
            }
            JWEHeader jweHeader = parameters.jweHeader();
            JWEObject jweObject = new JWEObject(jweHeader,
                new Payload(signedJwt));

            JWEEncrypter encrypter = createEncrypter(rsaKey);
            jweObject.encrypt(encrypter);
            return new Jwt(jweObject.serialize(),
                claims.getIssuedAt(), claims.getExpiresAt(), jweHeader.toJSONObject(), claims.getClaims());
        } catch (JOSEException e) {
            throw new JwtEncodingException(ENCODING_ERROR_MESSAGE_TEMPLATE.formatted("Unable to encrypt JWT"), e);
        }
    }

    /**
     * Converts {@link JwsHeader} to {@link JWSHeader} for Nimbus processing.
     *
     * @param headers the JwsHeader instance to convert
     * @return the converted JWSHeader instance
     */
    private static JWSHeader convert(JwsHeader headers) {
        JWSHeader.Builder builder = new JWSHeader.Builder(JWSAlgorithm.parse(headers.getAlgorithm().getName()));
        if (headers.getJwkSetUrl() != null) {
            builder.jwkURL(convertAsURI("jku", headers.getJwkSetUrl()));
        }

        Map<String, Object> jwk = headers.getJwk();
        if (!CollectionUtils.isEmpty(jwk)) {
            try {
                builder.jwk(JWK.parse(jwk));
            } catch (Exception ex) {
                throw new JwtEncodingException(
                    ENCODING_ERROR_MESSAGE_TEMPLATE.formatted("Unable to convert 'jwk' JOSE header"), ex);
            }
        }

        String keyId = headers.getKeyId();
        if (StringUtils.hasText(keyId)) {
            builder.keyID(keyId);
        }

        if (headers.getX509Url() != null) {
            builder.x509CertURL(convertAsURI("x5u", headers.getX509Url()));
        }

        List<String> x509CertificateChain = headers.getX509CertificateChain();
        if (!CollectionUtils.isEmpty(x509CertificateChain)) {
            builder.x509CertChain(convertCertificateChain(x509CertificateChain));
        }

        String x509SHA256Thumbprint = headers.getX509SHA256Thumbprint();
        if (StringUtils.hasText(x509SHA256Thumbprint)) {
            builder.x509CertSHA256Thumbprint(new Base64URL(x509SHA256Thumbprint));
        }

        String type = headers.getType();
        if (StringUtils.hasText(type)) {
            builder.type(new JOSEObjectType(type));
        }

        String contentType = headers.getContentType();
        if (StringUtils.hasText(contentType)) {
            builder.contentType(contentType);
        }

        Set<String> critical = headers.getCritical();
        if (!CollectionUtils.isEmpty(critical)) {
            builder.criticalParams(critical);
        }

        Map<String, Object> customHeaders = new HashMap<>();
        headers.getHeaders().forEach((name, value) -> {
            if (!JWSHeader.getRegisteredParameterNames().contains(name)) {
                customHeaders.put(name, value);
            }
        });
        if (!customHeaders.isEmpty()) {
            builder.customParams(customHeaders);
        }

        return builder.build();
    }

    /**
     * Converts {@link JwtClaimsSet} to {@link JWTClaimsSet} for Nimbus processing.
     *
     * @param claims the JwtClaimsSet instance to convert
     * @return the converted JWTClaimsSet instance
     */
    private static JWTClaimsSet convert(JwtClaimsSet claims) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        Object issuer = claims.getClaim("iss");
        if (issuer != null) {
            builder.issuer(issuer.toString());
        }

        String subject = claims.getSubject();
        if (StringUtils.hasText(subject)) {
            builder.subject(subject);
        }

        List<String> audience = claims.getAudience();
        if (!CollectionUtils.isEmpty(audience)) {
            builder.audience(audience);
        }

        Instant expiresAt = claims.getExpiresAt();
        if (expiresAt != null) {
            builder.expirationTime(Date.from(expiresAt));
        }

        Instant notBefore = claims.getNotBefore();
        if (notBefore != null) {
            builder.notBeforeTime(Date.from(notBefore));
        }

        Instant issuedAt = claims.getIssuedAt();
        if (issuedAt != null) {
            builder.issueTime(Date.from(issuedAt));
        }

        String jwtId = claims.getId();
        if (StringUtils.hasText(jwtId)) {
            builder.jwtID(jwtId);
        }

        Map<String, Object> customClaims = new HashMap<>();
        claims.getClaims().forEach((name, value) -> {
            if (!JWTClaimsSet.getRegisteredNames().contains(name)) {
                customClaims.put(name, value);
            }
        });
        if (!customClaims.isEmpty()) {
            Objects.requireNonNull(builder);
            customClaims.forEach(builder::claim);
        }

        return builder.build();
    }

    /**
     * Converts a {@link URL} to a {@link URI} for setting certain headers.
     *
     * @param header the header type (e.g., "jku" or "x5u")
     * @param url    the URL to convert
     * @return the converted URI
     */
    private static URI convertAsURI(String header, URL url) {
        try {
            return url.toURI();
        } catch (Exception ex) {
            throw new JwtEncodingException(ENCODING_ERROR_MESSAGE_TEMPLATE.formatted(
                "Unable to convert '" + header + "' JOSE header to a URI"), ex);
        }
    }

    /**
     * Converts a list of X.509 certificate chain strings to a list of {@link Base64} encoded values.
     * This is useful for setting the "x5c" header parameter in JWS and JWE headers.
     *
     * @param x509CertificateChain the list of X.509 certificates as strings in Base64 format
     * @return a list of {@link Base64} encoded certificate chain values
     */
    private static List<Base64> convertCertificateChain(List<String> x509CertificateChain) {
        List<Base64> x5cList = new ArrayList<>();
        x509CertificateChain.forEach(x5c -> x5cList.add(new Base64(x5c)));
        return x5cList;
    }

    /**
     * Selects the appropriate {@link JWK} based on the provided {@link JwsHeader}.
     *
     * @param headers the headers used for selecting the key
     * @return the selected {@link JWK} instance
     * @throws JwtEncodingException if multiple or no keys match the selection criteria
     */
    private JWK selectJwk(JwsHeader headers) {
        List jwks;
        try {
            JWKSelector jwkSelector = new JWKSelector(createJwkMatcher(headers));
            jwks = this.jwkSource.get(jwkSelector, null);
        } catch (Exception var4) {
            Exception ex = var4;
            throw new JwtEncodingException(ENCODING_ERROR_MESSAGE_TEMPLATE.formatted(
                "Failed to select a JWK signing key -> " + ex.getMessage()), ex);
        }

        if (jwks.size() > 1) {
            throw new JwtEncodingException(ENCODING_ERROR_MESSAGE_TEMPLATE.formatted(
                "Found multiple JWK signing keys for algorithm '" + headers.getAlgorithm().getName() + "'"));
        } else if (jwks.isEmpty()) {
            throw new JwtEncodingException(
                ENCODING_ERROR_MESSAGE_TEMPLATE.formatted("Failed to select a JWK signing key"));
        } else {
            return (JWK) jwks.getFirst();
        }
    }

    /**
     * Creates a {@link JWKMatcher} to select keys based on the {@link JwsHeader} configuration.
     *
     * @param headers the JwsHeader instance with key selection parameters
     * @return the configured {@link JWKMatcher} instance
     */
    private static JWKMatcher createJwkMatcher(JwsHeader headers) {
        JWSAlgorithm jwsAlgorithm = JWSAlgorithm.parse(headers.getAlgorithm().getName());
        if (!JWSAlgorithm.Family.RSA.contains(jwsAlgorithm) && !JWSAlgorithm.Family.EC.contains(jwsAlgorithm)) {
            return JWSAlgorithm.Family.HMAC_SHA.contains(jwsAlgorithm) ?
                (new JWKMatcher.Builder()).keyType(KeyType.forAlgorithm(jwsAlgorithm))
                    .keyID(headers.getKeyId()).privateOnly(true).algorithms(new Algorithm[] {jwsAlgorithm, null})
                    .build() : null;
        } else {
            return (new JWKMatcher.Builder()).keyType(KeyType.forAlgorithm(jwsAlgorithm)).keyID(headers.getKeyId())
                .keyUses(
                    KeyUse.SIGNATURE, null).algorithms(jwsAlgorithm, null)
                .x509CertSHA256Thumbprint(Base64URL.from(headers.getX509SHA256Thumbprint())).build();
        }
    }

    /**
     * Creates a {@link JWSSigner} for the specified {@link JWK}.
     *
     * @param jwk the JWK instance for which to create the signer
     * @return the created {@link JWSSigner} instance
     * @throws JwtEncodingException if the signer cannot be created
     */
    private static JWSSigner createSigner(JWK jwk) {
        try {
            return JWS_SIGNER_FACTORY.createJWSSigner(jwk);
        } catch (JOSEException var2) {
            throw new JwtEncodingException(ENCODING_ERROR_MESSAGE_TEMPLATE.formatted(
                "Failed to create a JWS Signer -> " + var2.getMessage()), var2);
        }
    }

    /**
     * Adds key identifier headers to {@link JwsHeader} if they are missing but available in the {@link JWK}.
     *
     * @param headers the current JwsHeader instance
     * @param jwk     the JWK containing key identifier information
     * @return a modified JwsHeader instance with key identifiers
     */
    private static JwsHeader addKeyIdentifierHeadersIfNecessary(JwsHeader headers, JWK jwk) {
        if (StringUtils.hasText(headers.getKeyId()) && StringUtils.hasText(headers.getX509SHA256Thumbprint())) {
            return headers;
        } else if (!StringUtils.hasText(jwk.getKeyID()) && jwk.getX509CertSHA256Thumbprint() == null) {
            return headers;
        } else {
            JwsHeader.Builder headersBuilder = JwsHeader.from(headers);
            if (!StringUtils.hasText(headers.getKeyId()) && StringUtils.hasText(jwk.getKeyID())) {
                headersBuilder.keyId(jwk.getKeyID());
            }

            if (!StringUtils.hasText(headers.getX509SHA256Thumbprint()) && jwk.getX509CertSHA256Thumbprint() != null) {
                headersBuilder.x509SHA256Thumbprint(jwk.getX509CertSHA256Thumbprint().toString());
            }

            return headersBuilder.build();
        }
    }

    /**
     * Creates an {@link RSAEncrypter} for JWE encryption using the provided RSA key.
     *
     * @param rsaKey the RSA key for encryption
     * @return the configured {@link RSAEncrypter}
     * @throws JOSEException if encryption setup fails
     */
    public JWEEncrypter createEncrypter(RSAKey rsaKey) throws JOSEException {
        return new RSAEncrypter(rsaKey);
    }

    static {
        JWS_SIGNER_FACTORY = new DefaultJWSSignerFactory();
    }
}
