package io.github.susimsek.springnextjssamples.config.logging;

import static io.github.susimsek.springnextjssamples.constant.Constants.SPRING_PROFILE_DEVELOPMENT;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.loki4j.logback.AbstractLoki4jEncoder;
import com.github.loki4j.logback.JavaHttpSender;
import com.github.loki4j.logback.JsonEncoder;
import com.github.loki4j.logback.Loki4jAppender;
import io.github.susimsek.springnextjssamples.aspect.LoggingAspect;
import io.github.susimsek.springnextjssamples.config.RequestMatcherConfig;
import io.github.susimsek.springnextjssamples.config.logging.formatter.JsonLogFormatter;
import io.github.susimsek.springnextjssamples.config.logging.formatter.LogFormatter;
import io.github.susimsek.springnextjssamples.config.logging.handler.HttpLoggingHandler;
import io.github.susimsek.springnextjssamples.config.logging.handler.LoggingHandler;
import io.github.susimsek.springnextjssamples.config.logging.strategy.DefaultObfuscationStrategy;
import io.github.susimsek.springnextjssamples.config.logging.strategy.NoOpObfuscationStrategy;
import io.github.susimsek.springnextjssamples.config.logging.strategy.ObfuscationStrategy;
import io.github.susimsek.springnextjssamples.config.logging.utils.Obfuscator;
import io.github.susimsek.springnextjssamples.config.logging.wrapper.HttpLoggingWrapper;
import io.github.susimsek.springnextjssamples.enums.FilterOrder;
import io.github.susimsek.springnextjssamples.web.filter.LoggingFilter;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(name = "logging.http.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class LoggingConfig {

    private final LoggingProperties loggingProperties;
    private final Environment env;

    private static final String LOKI_APPENDER_NAME = "LOKI";
    private static final String ASYNC_LOKI_APPENDER_NAME = "ASYNC_LOKI";

    @Configuration
    @ConditionalOnProperty(name = "logging.http.enabled", havingValue = "true", matchIfMissing = true)
    static class HttpLoggingConfig {

        @Bean
        public HttpLoggingWrapper httpLoggingWrapper(LoggingHandler loggingHandler) {
            return new HttpLoggingWrapper(loggingHandler);
        }

        @Bean
        public LoggingFilter loggingFilter(LoggingHandler loggingHandler) {
            LoggingFilter loggingFilter = new LoggingFilter(loggingHandler);
            loggingFilter.setOrder(FilterOrder.LOGGING.order());
            return loggingFilter;
        }
    }

    @Bean
    @ConditionalOnProperty(name = "logging.aspect.enabled", havingValue = "true", matchIfMissing = true)
    public LoggingAspect loggingAspect(LoggingHandler loggingHandler) {
        return new LoggingAspect(loggingHandler);
    }

    @Bean
    public LoggingHandler loggingHandler(LogFormatter logFormatter,
                                         Obfuscator obfuscator,
                                         RequestMatcherConfig requestMatcherConfig,
                                         Tracer tracer) {
        var builder = HttpLoggingHandler.builder(tracer, logFormatter, obfuscator)
            .httpLogLevel(loggingProperties.getHttp().getLogLevel())
            .methodLogLevel(loggingProperties.getAspect().getLogLevel())
            .requestMatchers(requestMatcherConfig.staticResourcePaths()).permitAll()
            .requestMatchers(requestMatcherConfig.swaggerResourcePaths()).permitAll()
            .requestMatchers(requestMatcherConfig.actuatorEndpoints()).permitAll()
            .requestMatchers(requestMatcherConfig.graphiqlResourcePath()).permitAll();

        if (env.acceptsProfiles(Profiles.of(SPRING_PROFILE_DEVELOPMENT))) {
            builder.requestMatchers("/h2-console/**").permitAll();
        }
        return builder.anyRequest()
            .logged()
            .build();
    }

    @Bean
    public LogFormatter logFormatter(ObjectProvider<ObjectMapper> objectMapperProvider) {
        ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
        return new JsonLogFormatter(objectMapper);
    }

    @Bean
    public ObfuscationStrategy obfuscationStrategy(ObjectMapper objectMapper) {
        if (loggingProperties.getObfuscate().isEnabled()) {
            return new DefaultObfuscationStrategy(loggingProperties, objectMapper);
        } else {
            return new NoOpObfuscationStrategy();
        }
    }

    @Bean
    public Obfuscator obfuscator(ObfuscationStrategy obfuscationStrategy) {
        return new Obfuscator(obfuscationStrategy);
    }

    @Configuration
    @ConditionalOnProperty(name = "logging.loki.enabled", havingValue = "true")
    @RequiredArgsConstructor
    static class LokiLoggingConfig {

        private final LoggingProperties loggingProperties;

        @Bean
        public AsyncAppender asyncLoki4jAppender(Environment environment) {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            var loki4jAppender = getLoki4jAppender(context, environment);

            AsyncAppender asyncAppender = getLokiAsyncAppender(context);
            asyncAppender.addAppender(loki4jAppender);
            asyncAppender.start();

            Logger rootLogger = context.getLogger(ROOT_LOGGER_NAME);
            rootLogger.addAppender(asyncAppender);
            return asyncAppender;
        }

        private AsyncAppender getLokiAsyncAppender(LoggerContext context) {
            AsyncAppender asyncAppender = new AsyncAppender();
            asyncAppender.setContext(context);
            asyncAppender.setName(ASYNC_LOKI_APPENDER_NAME);
            asyncAppender.setQueueSize(loggingProperties.getAsync().getQueueSize());
            asyncAppender.setDiscardingThreshold(loggingProperties.getAsync().getDiscardingThreshold());
            asyncAppender.setMaxFlushTime(loggingProperties.getAsync().getMaxFlushTime());
            asyncAppender.setIncludeCallerData(loggingProperties.getAsync().isIncludeCallerData());
            return asyncAppender;
        }

        private Loki4jAppender getLoki4jAppender(LoggerContext context,
                                                 Environment environment) {
            var loki4jAppender = new Loki4jAppender();
            loki4jAppender.setContext(context);
            loki4jAppender.setName(LOKI_APPENDER_NAME);

            var httpSender = getJavaHttpSender();
            loki4jAppender.setHttp(httpSender);

            var encoder = getJsonEncoder(context, environment);
            loki4jAppender.setFormat(encoder);

            var lokiProps = loggingProperties.getLoki();

            loki4jAppender.setBatchMaxItems(lokiProps.getBatchMaxItems());
            loki4jAppender.setBatchMaxBytes((int) lokiProps.getBatchMaxBytes().toBytes());
            loki4jAppender.setBatchTimeoutMs(lokiProps.getBatchTimeout().toMillis());

            LoggingProperties.Loki.Retry retryProps = lokiProps.getRetry();
            loki4jAppender.setMaxRetries(retryProps.getMaxRetries());
            loki4jAppender.setMinRetryBackoffMs(retryProps.getMinRetryBackoff().toMillis());
            loki4jAppender.setMaxRetryBackoffMs(retryProps.getMaxRetryBackoff().toMillis());
            loki4jAppender.setMaxRetryJitterMs((int) retryProps.getMaxRetryJitter().toMillis());

            loki4jAppender.setSendQueueMaxBytes(lokiProps.getBatchMaxBytes().toBytes() * 10);
            loki4jAppender.setInternalQueuesCheckTimeoutMs(lokiProps.getInternalQueuesCheckTimeout().toMillis());
            loki4jAppender.setDropRateLimitedBatches(lokiProps.isDropRateLimitedBatches());
            loki4jAppender.setMetricsEnabled(lokiProps.isMetricsEnabled());
            loki4jAppender.setDrainOnStop(lokiProps.isDrainOnStop());
            loki4jAppender.setUseDirectBuffers(lokiProps.isUseDirectBuffers());
            loki4jAppender.setVerbose(lokiProps.isVerbose());
            loki4jAppender.start();
            return loki4jAppender;
        }

        private JsonEncoder getJsonEncoder(LoggerContext context, Environment environment) {
            String applicationName = environment.getProperty("spring.application.name", "my-app");
            String applicationEnvironment = environment.getProperty("spring.profiles.active", "default");
            String hostname = environment.getProperty("HOSTNAME", "localhost");
            var encoder = new JsonEncoder();
            encoder.setContext(context);
            var label = getLabel(applicationName, hostname, applicationEnvironment);
            encoder.setLabel(label);
            encoder.setSortByTime(true);
            encoder.setMessage(getPatternLayout(context));
            encoder.start();
            return encoder;
        }

        private Layout<ILoggingEvent> getPatternLayout(LoggerContext context) {
            var layout = new ch.qos.logback.classic.PatternLayout();
            layout.setContext(context);
            layout.setPattern(loggingProperties.getPattern());
            layout.start();
            return layout;
        }

        public JavaHttpSender getJavaHttpSender() {
            JavaHttpSender httpSender = new JavaHttpSender();
            String lokiUrl = loggingProperties.getLoki().getUrl();
            httpSender.setUrl(lokiUrl);
            httpSender.setInnerThreadsExpirationMs(loggingProperties.getLoki().getInnerThreadsExpiration().toMillis());
            return httpSender;
        }

        private AbstractLoki4jEncoder.LabelCfg getLabel(String applicationName, String hostname,
                                                        String applicationEnvironment) {
            var label = new AbstractLoki4jEncoder.LabelCfg();
            label.setReadMarkers(true);
            String labelPattern = (
                "app=%s,host=%s,env=%s,level=%%level,"
                    + "traceId=%%X{traceId:-unknown},spanId=%%X{spanId:-unknown}").formatted(
                applicationName, hostname, applicationEnvironment
            );
            label.setPattern(labelPattern);
            return label;
        }
    }
}
