package io.github.susimsek.springnextjssamples.config.logging.wrapper;


import io.github.susimsek.springnextjssamples.client.interceptor.RestClientLoggingInterceptor;
import io.github.susimsek.springnextjssamples.config.logging.handler.LoggingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@RequiredArgsConstructor
public class HttpLoggingWrapper {

    private final LoggingHandler loggingHandler;

    public ClientHttpRequestInterceptor createRestClientInterceptor() {
        return new RestClientLoggingInterceptor(loggingHandler);
    }
}
