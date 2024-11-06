package io.github.susimsek.springnextjssamples.security.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.RemoteKeySourceException;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.JWTProcessor;
import io.github.susimsek.springnextjssamples.security.key.Key;
import io.github.susimsek.springnextjssamples.security.key.KeyService;
import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.util.Assert;

/**
 * {@code TokenDecoder} is a custom JWT decoder that supports decoding both signed (JWS) and encrypted (JWE) tokens.
 * This class leverages Nimbus JOSE+JWT library to parse, decrypt, and validate tokens, making it useful for
 * applications where tokens are encoded with complex security layers.
 *
 * <p>This decoder can handle tokens encrypted with RSA keys and performs validation on claims and headers.
 * It also supports setting custom token validators and claim set converters for specific use cases.</p>
 *
 * <p>Example usage:
 * <pre>
 *     TokenDecoder decoder = new TokenDecoder(keyService, jwtProcessor);
 *     Jwt decodedJwt = decoder.decode(token);
 * </pre>
 * </p>
 *
 * @see JwtDecoder
 * @see JWTProcessor
 */
@RequiredArgsConstructor
public final class TokenDecoder implements JwtDecoder {

    private static final String DECODING_ERROR_MESSAGE_TEMPLATE =
        "An error occurred while attempting to decode the Jwt: %s";

    private final Log logger = LogFactory.getLog(this.getClass());
    private final KeyService keyService;
    private final JWTProcessor<SecurityContext> jwtProcessor;
    private OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();
    private Converter<Map<String, Object>, Map<String, Object>> claimSetConverter =
        MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());


    /**
     * Sets a custom {@link OAuth2TokenValidator} for validating the decoded JWT.
     *
     * @param jwtValidator the JWT validator to set
     */
    public void setJwtValidator(OAuth2TokenValidator<Jwt> jwtValidator) {
        Assert.notNull(jwtValidator, "jwtValidator cannot be null");
        this.jwtValidator = jwtValidator;
    }

    /**
     * Sets a custom converter for JWT claims, allowing customization of claim mappings.
     *
     * @param claimSetConverter the claim set converter to set
     */
    public void setClaimSetConverter(Converter<Map<String, Object>, Map<String, Object>> claimSetConverter) {
        Assert.notNull(claimSetConverter, "claimSetConverter cannot be null");
        this.claimSetConverter = claimSetConverter;
    }

    /**
     * Decodes the given JWT token, handling both signed (JWS) and encrypted (JWE) tokens.
     * It decrypts the token if necessary, validates its structure, and returns the decoded {@link Jwt} instance.
     *
     * @param token the encoded JWT token to decode
     * @return the decoded {@link Jwt} instance
     * @throws JwtException if an error occurs during decoding or validation
     */
    @Override
    public Jwt decode(String token) throws JwtException {
        JWT jwt;
        if (isJweToken(token)) {
            JWEObject jweObject = this.parseJwe(token);
            JWEHeader jweHeader = jweObject.getHeader();
            String jweKeyId = jweHeader.getKeyID();
            Key oAuth2Key = keyService.findByKidOrThrow(jweKeyId);
            decrypt(jweObject, oAuth2Key.toRSAKey());
            jwt = getSignedJWT(jweObject);
        } else {
            jwt = this.parseJwt(token);
        }
        Jwt createdJwt = this.createJwt(token, jwt);
        return this.validateJwt(createdJwt);
    }

    /**
     * Parses the encrypted JWE token into a {@link JWEObject}.
     *
     * @param token the JWE token string
     * @return the parsed {@link JWEObject}
     */
    private JWEObject parseJwe(String token) {
        try {
            return JWEObject.parse(token);
        } catch (Exception ex) {
            this.logger.trace("Failed to parse token", ex);
            if (ex instanceof ParseException) {
                throw new BadJwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted("Malformed token"), ex);
            } else {
                throw new BadJwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted(ex.getMessage()), ex);
            }
        }
    }

    /**
     * Parses the signed JWS token into a {@link JWT}.
     *
     * @param token the JWS token string
     * @return the parsed {@link JWT}
     */
    private JWT parseJwt(String token) {
        try {
            return JWTParser.parse(token);
        } catch (Exception ex) {
            this.logger.trace("Failed to parse token", ex);
            if (ex instanceof ParseException) {
                throw new BadJwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted("Malformed token"), ex);
            } else {
                throw new BadJwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted(ex.getMessage()), ex);
            }
        }
    }

    /**
     * Decrypts the given {@link JWEObject} using the provided RSA key.
     *
     * @param jweObject the JWE object to decrypt
     * @param rsaKey the RSA key to use for decryption
     */
    private void decrypt(JWEObject jweObject, RSAKey rsaKey) {
        try {
            RSADecrypter decrypter = new RSADecrypter(rsaKey);
            jweObject.decrypt(decrypter);
        } catch (JOSEException e) {
            throw new JwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted("Unable to decrypt token"), e);
        }
    }

    /**
     * Extracts the {@link SignedJWT} payload from a {@link JWEObject}.
     *
     * @param jweObject the decrypted JWE object
     * @return the extracted {@link SignedJWT}
     */
    private SignedJWT getSignedJWT(JWEObject jweObject) {
        return jweObject.getPayload().toSignedJWT();
    }

    /**
     * Creates a {@link Jwt} instance from a parsed JWT, converting headers and claims to a Spring-compatible format.
     *
     * @param token the original JWT token string
     * @param parsedJwt the parsed {@link JWT} object
     * @return the created {@link Jwt} instance
     */
    private Jwt createJwt(String token, JWT parsedJwt) {
        try {
            JWTClaimsSet jwtClaimsSet = this.jwtProcessor.process(parsedJwt, null);
            Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
            Map<String, Object> claims = this.claimSetConverter.convert(jwtClaimsSet.getClaims());
            return Jwt.withTokenValue(token)
                .headers(h -> h.putAll(headers))
                .claims(c -> c.putAll(claims))
                .build();
        } catch (RemoteKeySourceException ex) {
            this.logger.trace("Failed to retrieve JWK set", ex);
            if (ex.getCause() instanceof ParseException) {
                throw new JwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted("Malformed Jwk set"), ex);
            } else {
                throw new JwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted(ex.getMessage()), ex);
            }
        } catch (JOSEException ex) {
            this.logger.trace("Failed to process JWT", ex);
            throw new JwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted(ex.getMessage()), ex);
        } catch (Exception ex) {
            this.logger.trace("Failed to process JWT", ex);
            if (ex.getCause() instanceof ParseException) {
                throw new BadJwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted("Malformed payload"), ex);
            } else {
                throw new BadJwtException(DECODING_ERROR_MESSAGE_TEMPLATE.formatted(ex.getMessage()), ex);
            }
        }
    }

    /**
     * Validates the decoded {@link Jwt} instance using the configured token validator.
     *
     * @param jwt the decoded JWT to validate
     * @return the validated {@link Jwt}
     * @throws JwtValidationException if validation fails
     */
    private Jwt validateJwt(Jwt jwt) {
        OAuth2TokenValidatorResult result = this.jwtValidator.validate(jwt);
        if (result.hasErrors()) {
            throw new JwtValidationException(getJwtValidationExceptionMessage(result.getErrors()), result.getErrors());
        }
        return jwt;
    }

    /**
     * Checks if the token is in JWE format by counting the number of token segments.
     *
     * @param token the JWT token string
     * @return {@code true} if the token is a JWE token, {@code false} otherwise
     */
    public boolean isJweToken(String token) {
        return token.split("\\.").length == 5;
    }

    /**
     * Constructs an error message from a collection of {@link OAuth2Error} objects.
     *
     * @param errors the errors to include in the message
     * @return the constructed error message
     */
    private String getJwtValidationExceptionMessage(Iterable<OAuth2Error> errors) {
        StringBuilder message = new StringBuilder("Unable to validate Jwt: ");
        for (OAuth2Error error : errors) {
            message.append(error.getDescription()).append(" ");
        }
        return message.toString().trim();
    }
}
