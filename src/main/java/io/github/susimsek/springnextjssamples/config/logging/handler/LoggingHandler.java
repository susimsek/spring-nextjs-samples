package io.github.susimsek.springnextjssamples.config.logging.handler;

import io.github.susimsek.springnextjssamples.config.logging.enums.Source;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;

public interface LoggingHandler {

    void logRequest(HttpMethod method, URI uri, HttpHeaders headers, byte[] body,
                    Source source);

    void logResponse(HttpMethod method, URI uri, Integer statusCode, HttpHeaders headers, byte[] responseBody,
                     Source source, long duration);

    void logMethodEntry(String className, String methodName, Object[] args);

    void logMethodExit(String className, String methodName, Object result, long duration);

    void logException(String className, String methodName, Object[] args,
                      String exceptionMessage, long duration);

    boolean shouldNotLog(HttpServletRequest request);

    boolean shouldNotLog(HttpRequest request);

    boolean shouldNotMethodLog(ProceedingJoinPoint joinPoint);
}
