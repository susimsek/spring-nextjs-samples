package io.github.susimsek.springnextjssamples.security.token;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import io.github.susimsek.springnextjssamples.security.AuthoritiesConstants;
import io.github.susimsek.springnextjssamples.security.SecurityProperties;
import io.github.susimsek.springnextjssamples.security.key.Key;
import io.github.susimsek.springnextjssamples.security.key.KeyService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * {@code TokenGenerator} is responsible for generating JWT tokens using the provided {@link TokenEncoder}.
 * This class can generate either a signed JWT or an encrypted JWT based on the application's security configuration.
 *
 * <p>This generator uses authentication details, security properties, and cryptographic keys to create
 * a JWT with claims such as issuer, subject, expiration, and custom authorities claims. It supports
 * both JSON Web Signature (JWS) for signing and JSON Web Encryption (JWE) for optional encryption.</p>
 *
 * <p>Example usage:
 * <pre>
 *     TokenGenerator generator = new TokenGenerator(tokenEncoder, keyService, securityProperties);
 *     Jwt token = generator.generate(authentication);
 * </pre>
 * </p>
 *
 * @see TokenEncoder
 * @see SecurityProperties
 * @see KeyService
 */
@RequiredArgsConstructor
public final class TokenGenerator {

    private final TokenEncoder tokenEncoder;
    private final KeyService keyService;
    private final SecurityProperties securityProperties;

    /**
     * Generates a JWT based on the specified {@link Authentication} and configured properties.
     * This method creates claims such as issuer, subject, issued at time, expiration, and custom claims for authorities.
     * If JWE (encryption) is enabled, the token will be both signed and encrypted.
     *
     * @param authentication the authentication object representing the authenticated user
     * @return the generated {@link Jwt} token
     */
    public Jwt generate(Authentication authentication) {
        var token = securityProperties.getToken();
        String issuer = token.getIssuer();

        Instant issuedAt = Instant.now();
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;
        JWEAlgorithm jweAlgorithm = JWEAlgorithm.RSA_OAEP_256;
        EncryptionMethod encryptionMethod = EncryptionMethod.A256GCM;
        Instant expiresAt = issuedAt.plus(token.getAccessTokenTimeToLive());

        String jweKeyId = token.getKeyId();

        // Build JWT claims
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer);
        }

        claimsBuilder.subject(authentication.getName())
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .id(UUID.randomUUID().toString())
            .notBefore(issuedAt);

        // Add authorities to claims
        var authorities = authentication.getAuthorities().stream()
            .map(Object::toString)
            .toList();
        if (!CollectionUtils.isEmpty(authorities)) {
            claimsBuilder.claim(AuthoritiesConstants.CLAIM_NAME, authorities);
        }

        // Build JWS and JWE headers
        JwsHeader.Builder jwsHeaderBuilder = JwsHeader.with(jwsAlgorithm);
        JwsHeader jwsHeader = jwsHeaderBuilder.build();

        JWEHeader.Builder jweHeaderBuilder = new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
            .contentType("JWT");
        if (jweKeyId != null) {
            jweHeaderBuilder.keyID(jweKeyId);
        }

        // Create JWT claims set
        JwtClaimsSet claims = claimsBuilder.build();
        Boolean jweEnabled = token.isJweEnabled();

        // Encode as signed JWT if JWE is not enabled
        if (Boolean.FALSE.equals(jweEnabled)) {
            return tokenEncoder.encode(TokenEncoderParameters.from(jwsHeader, null, claims), null);
        }

        // Encode as encrypted JWT
        Key key = keyService.findByKidOrThrow(jweKeyId);
        return tokenEncoder.encode(
            TokenEncoderParameters.from(jwsHeader, jweHeaderBuilder.build(), claims),
            key.toRSAKey());
    }
}
