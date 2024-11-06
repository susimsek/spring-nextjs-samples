package io.github.susimsek.springnextjssamples.exception.async;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

/**
 * A global exception handler for uncaught exceptions in asynchronous methods.
 * <p>
 * This component implements {@link AsyncUncaughtExceptionHandler} to provide a centralized
 * approach for logging uncaught exceptions that occur in asynchronous methods, which are
 * executed outside the main thread.
 * </p>
 */
@Slf4j
@Component
public class GlobalAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    /**
     * Handles uncaught exceptions thrown by asynchronous methods.
     * <p>
     * Logs the exception message, the name of the method where the exception occurred, and
     * the parameter values passed to the method.
     * </p>
     *
     * @param throwable the exception that was thrown
     * @param method the method where the exception occurred
     * @param obj the parameters passed to the method
     */
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        log.error("Exception message - {}", throwable.getMessage());
        log.error("Method name - {}", method.getName());
        for (Object param : obj) {
            log.error("Parameter value - {}", param);
        }
    }
}
