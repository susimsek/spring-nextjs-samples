package io.github.susimsek.springnextjssamples.logging.wrapper;


import io.github.susimsek.springnextjssamples.interceptor.RestClientLoggingInterceptor;
import io.github.susimsek.springnextjssamples.logging.handler.LoggingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@RequiredArgsConstructor
public class HttpLoggingWrapper {

    private final LoggingHandler loggingHandler;

    public ClientHttpRequestInterceptor createRestClientInterceptor() {
        return new RestClientLoggingInterceptor(loggingHandler);
    }
}
