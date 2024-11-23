package io.github.susimsek.springnextjssamples.security.token;

import com.nimbusds.jose.JWEHeader;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.util.Assert;

/**
 * {@code TokenEncoderParameters} holds the parameters needed for encoding a JWT, including the JWS and JWE headers
 * and the claims set. This class supports creating parameters for both signed and encrypted JWTs.
 *
 * <p>This class is immutable and provides factory methods for easily creating instances with either
 * a minimal set of claims or with specified JWS and JWE headers.</p>
 *
 * <p>Example usage:
 * <pre>
 *     JwtClaimsSet claims = JwtClaimsSet.builder().claim("sub", "1234567890").build();
 *     TokenEncoderParameters parameters = TokenEncoderParameters.from(claims);
 * </pre>
 * </p>
 *
 * @see JwsHeader
 * @see JWEHeader
 * @see JwtClaimsSet
 */
public record TokenEncoderParameters(
    @Nullable JwsHeader jwsHeader,
    @Nullable JWEHeader jweHeader,
    JwtClaimsSet claims) {

    /**
     * Creates a {@code TokenEncoderParameters} instance with the specified claims.
     *
     * @param claims the claims to include in the JWT
     * @return a new {@code TokenEncoderParameters} instance
     * @throws IllegalArgumentException if {@code claims} is {@code null}
     */
    public static TokenEncoderParameters from(JwtClaimsSet claims) {
        Assert.notNull(claims, "claims cannot be null");
        return new TokenEncoderParameters(null, null, claims);
    }

    /**
     * Creates a {@code TokenEncoderParameters} instance with the specified JWS header, JWE header, and claims.
     *
     * @param jwsHeader the JWS header for signing the JWT
     * @param jweHeader the JWE header for encrypting the JWT (optional)
     * @param claims    the claims to include in the JWT
     * @return a new {@code TokenEncoderParameters} instance
     * @throws IllegalArgumentException if {@code jwsHeader} or {@code claims} are {@code null}
     */
    public static TokenEncoderParameters from(JwsHeader jwsHeader, JWEHeader jweHeader, JwtClaimsSet claims) {
        Assert.notNull(jwsHeader, "jwsHeader cannot be null");
        Assert.notNull(claims, "claims cannot be null");
        return new TokenEncoderParameters(jwsHeader, jweHeader, claims);
    }
}
