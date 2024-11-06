package io.github.susimsek.springnextjssamples.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for security settings.
 * <p>
 * Provides properties for configuring security policies, including Content Security Policy (CSP)
 * and token-based authentication settings.
 * </p>
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * The Content Security Policy (CSP) string used to define content loading policies.
     * <p>
     * Must be a non-blank string as per validation constraints.
     * </p>
     */
    @NotBlank(message = "{validation.field.notBlank}")
    private String contentSecurityPolicy;

    /**
     * Nested configuration properties for token settings.
     */
    @Valid
    private Token token;

    /**
     * Configuration properties for token-based authentication settings.
     * <p>
     * Includes properties to configure JSON Web Encryption (JWE) enablement, token issuer,
     * access token time-to-live, and optional key identifier.
     * </p>
     */
    @Getter
    @Setter
    public static class Token {

        /**
         * Indicates whether JSON Web Encryption (JWE) is enabled for tokens.
         * Default is {@code true}.
         */
        private boolean jweEnabled = true;

        /**
         * The issuer of the token, required for token validation.
         * <p>
         * Must be a non-blank string as per validation constraints.
         * </p>
         */
        @NotBlank(message = "{validation.field.notBlank}")
        private String issuer;

        /**
         * The time-to-live duration for the access token.
         * <p>
         * Default is one hour. Must be a non-null value as per validation constraints.
         * </p>
         */
        @NotNull(message = "{validation.field.notNull}")
        private Duration accessTokenTimeToLive = Duration.ofHours(1);

        /**
         * The optional key identifier used for token signing or encryption.
         */
        private String keyId;
    }
}
