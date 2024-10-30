package io.github.susimsek.springnextjssamples.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    @NotBlank(message = "{validation.field.notBlank}")
    private String contentSecurityPolicy;

    @Valid
    private Token token;


    @Getter
    @Setter
    public static class Token {
        private boolean jweEnabled = true;

        @NotBlank(message = "{validation.field.notBlank}")
        private String issuer;

        @NotNull(message = "{validation.field.notNull}")
        private Duration accessTokenTimeToLive = Duration.ofHours(1);

        private String keyId;
    }


}
