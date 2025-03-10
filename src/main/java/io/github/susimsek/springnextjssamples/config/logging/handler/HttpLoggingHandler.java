package io.github.susimsek.springnextjssamples.config.logging.handler;

import io.github.susimsek.springnextjssamples.config.logging.enums.HttpLogLevel;
import io.github.susimsek.springnextjssamples.config.logging.enums.HttpLogType;
import io.github.susimsek.springnextjssamples.config.logging.enums.MethodLogLevel;
import io.github.susimsek.springnextjssamples.config.logging.enums.MethodLogType;
import io.github.susimsek.springnextjssamples.config.logging.enums.Source;
import io.github.susimsek.springnextjssamples.config.logging.formatter.LogFormatter;
import io.github.susimsek.springnextjssamples.config.logging.model.HttpLog;
import io.github.susimsek.springnextjssamples.config.logging.model.MethodLog;
import io.github.susimsek.springnextjssamples.config.logging.utils.Obfuscator;
import io.github.susimsek.springnextjssamples.config.logging.wrapper.HttpRequestMatcher;
import io.github.susimsek.springnextjssamples.config.tracing.Trace;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

@Slf4j
@RequiredArgsConstructor
public class HttpLoggingHandler implements LoggingHandler {
    private final Tracer tracer;
    private final HttpLogLevel httpLogLevel;
    private final MethodLogLevel methodLogLevel;
    private final LogFormatter logFormatter;
    private final Obfuscator obfuscator;
    private final List<RequestMatcherConfig> requestMatcherConfigs;
    private final boolean defaultLogged;

    @Override
    public void logRequest(HttpMethod method, URI uri, HttpHeaders headers, byte[] body,
                           Source source) {
        if (httpLogLevel == HttpLogLevel.NONE) {
            return;
        }
        HttpLog.HttpLogBuilder logBuilder = initLogBuilder(
            HttpLogType.REQUEST, method, uri, headers, source);
        if (isHttpLogLevel(HttpLogLevel.FULL)) {
            logBuilder.body(obfuscator.maskBody(new String(body, StandardCharsets.UTF_8)));
        }

        log("HTTP Request: {}", logFormatter.format(logBuilder.build()));
    }

    @Override
    public void logResponse(HttpMethod method, URI uri, Integer statusCode, HttpHeaders headers,
                            byte[] responseBody, Source source, long duration) {
        if (httpLogLevel == HttpLogLevel.NONE) {
            return;
        }
        HttpLog.HttpLogBuilder logBuilder = initLogBuilder(
            HttpLogType.RESPONSE, method, uri, headers, source).statusCode(statusCode)
            .durationMs(duration);

        HttpStatus status = HttpStatus.valueOf(statusCode);

        if (isHttpLogLevel(HttpLogLevel.FULL) && status.is2xxSuccessful()) {
            logBuilder.body(obfuscator.maskBody(new String(responseBody, StandardCharsets.UTF_8)));
        } else if (shouldLogWithoutBody(status)) {
            logBuilder.body(null);
        } else if (status.is4xxClientError() || status.is5xxServerError()) {
            logBuilder.body(obfuscator.maskBody(new String(responseBody, StandardCharsets.UTF_8)));
        }

        log("HTTP Response: {}", logFormatter.format(logBuilder.build()));
    }

    @Override
    public void logMethodEntry(String className, String methodName, Object[] args) {
        if (!isMethodLogLevel(MethodLogLevel.BASIC)) {
            return;
        }
        MethodLog.MethodLogBuilder logBuilder = initMethodLogBuilder(
            MethodLogType.METHOD_ENTRY, className, methodName);
        if (isMethodLogLevel(MethodLogLevel.FULL)) {
            logBuilder.arguments(obfuscator.maskArguments(args));
        }
        log("Method Entry: {}", logFormatter.format(logBuilder.build()));
    }

    @Override
    public void logMethodExit(String className, String methodName, Object result, long duration) {
        if (!isMethodLogLevel(MethodLogLevel.BASIC)) {
            return;
        }
        MethodLog.MethodLogBuilder logBuilder = initMethodLogBuilder(
            MethodLogType.METHOD_EXIT, className, methodName)
            .durationMs(duration);
        if (isMethodLogLevel(MethodLogLevel.FULL)) {
            logBuilder.result(obfuscator.maskResult(result));
        }
        log("Method Exit: {}", logFormatter.format(logBuilder.build()));
    }

    @Override
    public void logException(String className, String methodName,
                             Object[] args, String exceptionMessage, long duration) {
        if (!isMethodLogLevel(MethodLogLevel.EXCEPTION)) {
            return;
        }
        MethodLog.MethodLogBuilder logBuilder = initMethodLogBuilder(
            MethodLogType.EXCEPTION, className, methodName)
            .exceptionMessage(exceptionMessage)
            .durationMs(duration);
        log("Exception in method: {}", logFormatter.format(logBuilder.build()));
    }

    @Override
    public boolean shouldNotLog(HttpServletRequest request) {
        return requestMatcherConfigs.stream()
            .filter(config -> config.requestMatcher.matches(request))
            .map(config -> !config.logged)
            .findFirst()
            .orElse(!defaultLogged);
    }

    @Override
    public boolean shouldNotLog(HttpRequest request) {
        return httpLogLevel == HttpLogLevel.NONE
            || requestMatcherConfigs.stream()
            .filter(config -> config.requestMatcher.matches(request))
            .map(config -> !config.logged)
            .findFirst()
            .orElse(!defaultLogged);
    }

    @Override
    public boolean shouldNotMethodLog(ProceedingJoinPoint joinPoint) {
        return methodLogLevel == MethodLogLevel.NONE;
    }

    private HttpLog.HttpLogBuilder initLogBuilder(HttpLogType type, HttpMethod method, URI uri,
                                                  HttpHeaders headers, Source source) {
        return HttpLog.builder()
            .type(type)
            .method(method)
            .uri(uri)
            .headers(isHttpLogLevel(HttpLogLevel.HEADERS) ? obfuscator.maskHeaders(headers) : new HttpHeaders())
            .source(source)
            .trace(type == HttpLogType.REQUEST ? createTrace(headers) : null);
    }

    private MethodLog.MethodLogBuilder initMethodLogBuilder(MethodLogType type,
                                                            String className, String methodName) {
        Trace trace = createTrace(null);

        return MethodLog.builder()
            .type(type)
            .className(className)
            .methodName(methodName)
            .trace(trace);
    }

    private boolean isHttpLogLevel(HttpLogLevel level) {
        return httpLogLevel.ordinal() >= level.ordinal();
    }

    private boolean isMethodLogLevel(MethodLogLevel level) {
        return methodLogLevel.ordinal() >= level.ordinal();
    }

    private boolean shouldLogWithoutBody(HttpStatus status) {
        return status == HttpStatus.UNAUTHORIZED || status == HttpStatus.FORBIDDEN
            || status == HttpStatus.TOO_MANY_REQUESTS;
    }

    private void log(String message, String formattedLog) {
        log.info(message, formattedLog);
    }

    private Trace createTrace(HttpHeaders headers) {
        Span currentSpan = tracer.currentSpan();
        String traceId = null;
        String spanId = null;
        if (currentSpan != null) {
            var traceContext = currentSpan.context();
            traceId = traceContext.traceId();
            spanId = traceContext.spanId();
        }
        return Trace.builder()
            .traceId(traceId)
            .spanId(spanId)
            .build();
    }

    @AllArgsConstructor
    private static class RequestMatcherConfig {
        private final HttpRequestMatcher requestMatcher;
        private boolean logged;
    }

    public interface InitialBuilder {
        InitialBuilder httpLogLevel(HttpLogLevel logLevel);

        InitialBuilder methodLogLevel(MethodLogLevel logLevel);

        AfterRequestMatchersBuilder anyRequest();

        AfterRequestMatchersBuilder requestMatchers(HttpMethod method, String... patterns);

        AfterRequestMatchersBuilder requestMatchers(String... patterns);

        HttpLoggingHandler build();
    }

    public interface AfterRequestMatchersBuilder {
        InitialBuilder permitAll();

        InitialBuilder logged();
    }

    public static InitialBuilder builder(Tracer tracer, LogFormatter logFormatter,
                                         Obfuscator obfuscator) {
        return new Builder(tracer, logFormatter, obfuscator);
    }

    private static class Builder extends AbstractRequestMatcherRegistry<Builder>
        implements InitialBuilder, AfterRequestMatchersBuilder {

        private final Tracer tracer;
        private final LogFormatter logFormatter;
        private final Obfuscator obfuscator;
        private final List<RequestMatcherConfig> requestMatcherConfigs = new ArrayList<>();
        private boolean anyRequestConfigured = false;
        private boolean defaultLogged = true;
        private HttpLogLevel httpLogLevel = HttpLogLevel.FULL;
        private MethodLogLevel methodLogLevel = MethodLogLevel.FULL;
        private int lastIndex = 0;

        private Builder(Tracer tracer, LogFormatter logFormatter,
                        Obfuscator obfuscator) {
            this.tracer = tracer;
            this.logFormatter = logFormatter;
            this.obfuscator = obfuscator;
        }

        @Override
        public Builder requestMatchers(HttpMethod method, String... patterns) {
            lastIndex = requestMatcherConfigs.size();
            for (String pattern : patterns) {
                this.requestMatcherConfigs.add(new RequestMatcherConfig(new HttpRequestMatcher.Builder()
                    .pattern(method, pattern).build(), true));
            }
            return this;
        }

        @Override
        public Builder requestMatchers(String... patterns) {
            lastIndex = requestMatcherConfigs.size();
            for (String pattern : patterns) {
                this.requestMatcherConfigs.add(new RequestMatcherConfig(new HttpRequestMatcher.Builder()
                    .pattern(pattern).build(), true));
            }
            return this;
        }

        @Override
        public Builder anyRequest() {
            Assert.state(!this.anyRequestConfigured, "anyRequest() can only be called once");
            this.anyRequestConfigured = true;
            return this;
        }

        public Builder permitAll() {
            Assert.state(anyRequestConfigured || !requestMatcherConfigs.isEmpty(),
                "permitAll() can only be called after requestMatchers() or anyRequest()");
            if (anyRequestConfigured) {
                this.defaultLogged = false;
            } else {
                requestMatcherConfigs.stream()
                    .skip(lastIndex)
                    .forEach(config -> config.logged = false);
            }
            return this;
        }

        public Builder logged() {
            Assert.state(anyRequestConfigured || !requestMatcherConfigs.isEmpty(),
                "logged() can only be called after requestMatchers() or anyRequest())");
            if (anyRequestConfigured) {
                this.defaultLogged = true;
            } else {
                requestMatcherConfigs.stream()
                    .skip(lastIndex)
                    .forEach(config -> config.logged = true);
            }
            return this;
        }

        public Builder httpLogLevel(HttpLogLevel logLevel) {
            this.httpLogLevel = logLevel;
            return this;
        }

        public Builder methodLogLevel(MethodLogLevel logLevel) {
            this.methodLogLevel = logLevel;
            return this;
        }

        public HttpLoggingHandler build() {
            return new HttpLoggingHandler(
                tracer, httpLogLevel, methodLogLevel, logFormatter, obfuscator,
                requestMatcherConfigs, defaultLogged);
        }

        @Override
        protected Builder chainRequestMatchers(List<RequestMatcher> requestMatchers) {
            this.requestMatchers(requestMatchers.toArray(new RequestMatcher[0]));
            return this;
        }
    }
}
