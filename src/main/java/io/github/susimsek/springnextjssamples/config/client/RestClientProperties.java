package io.github.susimsek.springnextjssamples.config.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "spring.restclient")
public class RestClientProperties {

    @NotNull(message = "{validation.field.notNull}")
    private Duration connectTimeout;

    @NotNull(message = "{validation.field.notNull}")
    private Duration readTimeout;

    @Valid
    @NotNull(message = "{validation.field.notNull}")
    private Map<String, ClientConfig> clients = new HashMap<>();

    @Getter
    @Setter
    public static class ClientConfig {

        @NotBlank(message = "{validation.field.notBlank}")
        @URL(message = "{validation.field.url}")
        @Size(max = 256, message = "{validation.field.size}")
        private String url;
    }
}
