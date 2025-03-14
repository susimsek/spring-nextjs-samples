package io.github.susimsek.springnextjssamples.config.logging.model;

import io.github.susimsek.springnextjssamples.config.logging.enums.HttpLogType;
import io.github.susimsek.springnextjssamples.config.logging.enums.Source;
import io.github.susimsek.springnextjssamples.config.tracing.Trace;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpLog {
    private HttpLogType type;
    private HttpMethod method;
    private URI uri;
    private Integer statusCode;
    private HttpHeaders headers;
    private String body;
    private Source source;
    private Trace trace;
    private Long durationMs;
}
