package io.github.susimsek.springnextjssamples.exception.graphql;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.github.susimsek.springnextjssamples.config.i18n.ParameterMessageSource;
import io.github.susimsek.springnextjssamples.exception.ErrorCode;
import io.github.susimsek.springnextjssamples.security.SecurityUtils;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GraphQLErrorUtils {

    private final ParameterMessageSource messageSource;

    public GraphQLError handleExceptionInternal(
        @NonNull Throwable ex,
        ErrorClassification errorClassification,
        Map<String, Object> extensions,
        String messageKey,
        Map<String, Object> namedArgs,
        Locale locale) {
        log.error("A GraphQL exception occurred, resulting in a response with classification {}",
            errorClassification, ex);
        return createGraphQLError(
            errorClassification,
            extensions,
            messageKey,
            namedArgs,
            locale
        );
    }

    public GraphQLError resolveSecurityException(
        Throwable ex,
        Locale locale
    ) {
        return SecurityUtils.isAuthenticated()
            ? resolveAccessDenied(ex, locale)
            : resolveUnauthorized(ex, locale);
    }


    public GraphQLError createGraphQLError(
        ErrorClassification errorClassification,
        Map<String, Object> extensions,
        String messageKey,
        Map<String, Object> namedArgs,
        Locale locale) {
        String errorMessage = messageSource.getMessageWithNamedArgs(
            messageKey, namedArgs, locale);
        return GraphqlErrorBuilder.newError()
            .errorType(errorClassification)
            .message(errorMessage)
            .extensions(extensions)
            .build();
    }

    public GraphQLError resolveAccessDenied(Throwable ex, Locale locale) {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        return handleExceptionInternal(ex, ErrorType.FORBIDDEN,
            Map.of("error", errorCode.errorCode()), errorCode.messageKey(), null, locale);
    }

    public GraphQLError resolveUnauthorized(Throwable ex,  Locale locale) {
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        return handleExceptionInternal(ex, ErrorType.UNAUTHORIZED,
            Map.of("error", errorCode.errorCode()), errorCode.messageKey(), null, locale);
    }

    public GraphQLError resolveServerError(Throwable ex, Locale locale) {
        ErrorCode errorCode = ErrorCode.SERVER_ERROR;
        return handleExceptionInternal(
            ex,
            ErrorType.INTERNAL_ERROR,
            Map.of("error", errorCode.errorCode()),
            errorCode.messageKey(),
            null,
            locale
        );
    }
}
