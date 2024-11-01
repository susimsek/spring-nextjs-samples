package io.github.susimsek.springnextjssamples.aspect;

import io.github.susimsek.springnextjssamples.logging.handler.LoggingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

    private final LoggingHandler loggingHandler;

    @Pointcut(
        "@within(io.github.susimsek.springnextjssamples.logging.annotation.Loggable) || "
            + "@annotation(io.github.susimsek.springnextjssamples.logging.annotation.Loggable)"
    )
    public void loggablePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut(
        "within(io.github.susimsek.springnextjssamples.repository..*)"
            + " || within(io.github.susimsek.springnextjssamples.service..*)"
            + " || within(io.github.susimsek.springnextjssamples.controller..*)"
    )
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

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

        // Log method entry
        loggingHandler.logMethodEntry(className, methodName, args);

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            long duration = stopWatch.getTotalTimeMillis();

            // Log method exit
            loggingHandler.logMethodExit(className, methodName, result, duration);
            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            long duration = stopWatch.getTotalTimeMillis();
            loggingHandler.logException(className, methodName, args, e.getMessage(), duration);
            throw e;
        }
    }
}
