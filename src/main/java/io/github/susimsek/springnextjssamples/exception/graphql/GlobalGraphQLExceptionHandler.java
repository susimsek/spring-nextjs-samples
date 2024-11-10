package io.github.susimsek.springnextjssamples.exception.graphql;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import io.github.susimsek.springnextjssamples.config.i18n.ParameterMessageSource;
import io.github.susimsek.springnextjssamples.exception.ErrorCode;
import io.github.susimsek.springnextjssamples.exception.LocalizedException;
import io.github.susimsek.springnextjssamples.exception.ResourceException;
import io.github.susimsek.springnextjssamples.exception.Violation;
import io.github.susimsek.springnextjssamples.exception.ratelimit.RateLimitExceededException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalGraphQLExceptionHandler {

    private final GraphQLErrorUtils graphQLErrorUtils;
    private final ParameterMessageSource messageSource;

    @GraphQlExceptionHandler(MethodArgumentNotValidException.class)
    public GraphQLError handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                     @NonNull DataFetchingEnvironment environment) {
        List<Violation> violations = Stream.concat(
            ex.getFieldErrors().stream().map(Violation::new),
            ex.getGlobalErrors().stream().map(Violation::new)
        ).toList();

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

        return graphQLErrorUtils.handleExceptionInternal(
            ex,
            ErrorType.BAD_REQUEST,
            Map.of(
                "error", errorCode.errorCode(),
                "violations", violations
            ),
            errorCode.messageKey(),
            null,
            environment.getLocale()
        );
    }

    @GraphQlExceptionHandler(ConstraintViolationException.class)
    public GraphQLError handleConstraintViolationException(@NonNull ConstraintViolationException ex,
                                                           @NonNull DataFetchingEnvironment environment) {
        List<Violation> violations = ex.getConstraintViolations().stream()
            .map(Violation::new)
            .toList();

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

        return graphQLErrorUtils.handleExceptionInternal(
            ex,
            ErrorType.BAD_REQUEST,
            Map.of(
                "error", errorCode.errorCode(),
                "violations", violations
            ),
            errorCode.messageKey(),
            null,
            environment.getLocale()
        );
    }

    @GraphQlExceptionHandler({
        AuthenticationException.class,
        AccessDeniedException.class
    })
    public GraphQLError handleSecurityException(@NonNull Exception ex,
                                                @NonNull DataFetchingEnvironment environment) {
        return graphQLErrorUtils.resolveSecurityException(ex, environment.getLocale());
    }

    @GraphQlExceptionHandler(RateLimitExceededException.class)
    public GraphQLError handleRateLimitExceededException(@NonNull RateLimitExceededException ex,
                                                         @NonNull DataFetchingEnvironment environment) {
        ErrorCode errorCode = ErrorCode.RATE_LIMIT_EXCEEDED;

        Map<String, Object> extensions = Map.of(
            "error", errorCode.errorCode(),
            HttpHeaders.RETRY_AFTER, String.valueOf(ex.getWaitTime()),
            "limit", ex.getLimitForPeriod(),
            "remaining", ex.getAvailablePermissions(),
            "reset", ex.getResetTime()
        );

        return graphQLErrorUtils.handleExceptionInternal(
            ex,
            ErrorType.THROTTLED,
            extensions,
            errorCode.messageKey(),
            null,
            environment.getLocale()
        );
    }

    @GraphQlExceptionHandler(ResourceException.class)
    public GraphQLError handleResourceException(@NonNull ResourceException ex,
                                                @NonNull DataFetchingEnvironment environment) {
        String localizedResourceName = messageSource.getMessage(
            "resource." + ex.getResourceName().toLowerCase(), null, environment.getLocale());
        String searchCriteria = messageSource.getMessage(
            "search.criteria." + ex.getSearchCriteria().toLowerCase(), null, environment.getLocale());
        Map<String, Object> namedArgs = createNamedArgs(
            localizedResourceName, searchCriteria, ex.getSearchValue());


        ErrorType errorType;
        if (ex.getStatus() == HttpStatus.NOT_FOUND) {
            errorType = ErrorType.NOT_FOUND; // 404 Not Found
        } else if (ex.getStatus() == HttpStatus.CONFLICT) {
            errorType = ErrorType.CONFLICT; // 409 Conflict
        } else {
            errorType = ErrorType.INTERNAL_ERROR;
        }

        return graphQLErrorUtils.handleExceptionInternal(
            ex,
            errorType,
            Map.of(
                "error", ex.getErrorCode()
            ),
            ex.getMessage(),
            namedArgs,
            environment.getLocale()
        );
    }

    @GraphQlExceptionHandler(LocalizedException.class)
    public GraphQLError handleLocalizedException(@NonNull LocalizedException ex,
                                                 @NonNull DataFetchingEnvironment environment) {
        ErrorType errorType = switch (ex.getStatus()) {
            case BAD_REQUEST -> ErrorType.BAD_REQUEST;
            case UNAUTHORIZED -> ErrorType.UNAUTHORIZED;
            default -> ErrorType.INTERNAL_ERROR;
        };

        return graphQLErrorUtils.handleExceptionInternal(ex, errorType,
            Map.of("error", ex.getErrorCode()), ex.getMessage(), ex.getNamedArgs(), environment.getLocale());
    }

    @GraphQlExceptionHandler
    public GraphQLError handleAllExceptions(@NonNull Exception ex,
                                            @NonNull DataFetchingEnvironment environment) {
        return graphQLErrorUtils.resolveServerError(ex, environment.getLocale());
    }

    private Map<String, Object> createNamedArgs(String resourceName,
                                               String searchCriteria,
                                               Object searchValue) {
        Map<String, Object> namedArgs = new HashMap<>();
        namedArgs.put("resource", resourceName);
        namedArgs.put("criteria", searchCriteria);
        namedArgs.put("value", searchValue.toString());
        return namedArgs;
    }
}
