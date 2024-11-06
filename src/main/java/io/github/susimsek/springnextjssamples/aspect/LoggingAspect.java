package io.github.susimsek.springnextjssamples.aspect;

import io.github.susimsek.springnextjssamples.config.logging.handler.LoggingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

/**
 * Aspect for logging execution of service, repository, and controller beans.
 * Logs entry, exit, and exception events for methods annotated with the {@link io.github.susimsek.springnextjssamples.config.logging.annotation.Loggable}
 * annotation or within specific packages.
 * <p>
 * The aspect utilizes a {@link LoggingHandler} to perform the actual logging.
 * Additionally, it measures the execution time of methods and logs it upon method exit.
 */
@Aspect
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

    private final LoggingHandler loggingHandler;

    /**
     * Pointcut that matches methods annotated with {@link io.github.susimsek.springnextjssamples.config.logging.annotation.Loggable}
     * or within classes annotated with {@link io.github.susimsek.springnextjssamples.config.logging.annotation.Loggable}.
     * This is used to trigger logging on specific methods or classes.
     */
    @Pointcut(
        "@within(io.github.susimsek.springnextjssamples.config.logging.annotation.Loggable) || "
            + "@annotation(io.github.susimsek.springnextjssamples.config.logging.annotation.Loggable)"
    )
    public void loggablePointcut() {
        // Pointcut marker method, implementations are in the advices.
    }

    /**
     * Pointcut that matches all methods within repository, service, and controller packages.
     * This is used to apply logging across these application layers.
     */
    @Pointcut(
        "within(io.github.susimsek.springnextjssamples.repository..*)"
            + " || within(io.github.susimsek.springnextjssamples.service..*)"
            + " || within(io.github.susimsek.springnextjssamples.web.controller..*)"
    )
    public void applicationPackagePointcut() {
        // Pointcut marker method, implementations are in the advices.
    }

    /**
     * Advice that logs method entry, exit, exceptions, and execution time.
     * This advice is applied to methods that satisfy both the {@code applicationPackagePointcut()} and {@code loggablePointcut()} conditions.
     *
     * @param joinPoint the join point representing the method invocation
     * @return the result of the method invocation
     * @throws Throwable if the target method throws an exception
     */
    @Around("applicationPackagePointcut() && loggablePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (loggingHandler.shouldNotMethodLog(joinPoint)) {
            return joinPoint.proceed();
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // Log method entry with parameters
        loggingHandler.logMethodEntry(className, methodName, args);

        try {
            Object result = joinPoint.proceed(); // Proceed with method execution
            stopWatch.stop();
            long duration = stopWatch.getTotalTimeMillis();

            // Log method exit with result and duration
            loggingHandler.logMethodExit(className, methodName, result, duration);
            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            long duration = stopWatch.getTotalTimeMillis();
            // Log exception with error message and duration
            loggingHandler.logException(className, methodName, args, e.getMessage(), duration);
            throw e;
        }
    }
}
