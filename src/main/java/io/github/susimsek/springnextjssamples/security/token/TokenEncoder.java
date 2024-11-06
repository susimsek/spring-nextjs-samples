package io.github.susimsek.springnextjssamples.security.token;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncodingException;

/**
 * {@code TokenEncoder} defines a functional interface for encoding a JWT (JSON Web Token) with
 * specified parameters and an RSA key for signing or encryption.
 *
 * <p>Implementations of this interface are expected to handle the encoding of tokens based on
 * the provided {@link TokenEncoderParameters} and {@link RSAKey}, supporting use cases where
 * secure token generation is required.</p>
 *
 * <p>Example usage:
 * <pre>
 *     TokenEncoder encoder = new DomainTokenEncoder(jwkSource);
 *     Jwt jwt = encoder.encode(parameters, rsaKey);
 * </pre>
 * </p>
 *
 * @see Jwt
 * @see TokenEncoderParameters
 */
@FunctionalInterface
public interface TokenEncoder {

    /**
     * Encodes a JWT with the specified parameters and RSA key.
     *
     * @param parameters the token encoding parameters, including claims and headers
     * @param rsaKey the RSA key to use for signing or encryption
     * @return the encoded {@link Jwt} instance
     * @throws JwtEncodingException if an error occurs during encoding
     */
    Jwt encode(TokenEncoderParameters parameters, RSAKey rsaKey) throws JwtEncodingException;
}
