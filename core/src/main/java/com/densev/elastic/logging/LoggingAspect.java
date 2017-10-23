package com.densev.elastic.logging;

import com.google.common.base.Stopwatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created on 10/17/2017.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("@annotation(com.densev.elastic.logging.annotations.LogParams)")
    public void logParamsAdvice(JoinPoint joinPoint) throws Throwable {
        logParams(joinPoint);
    }

    @AfterReturning(pointcut = "@annotation(com.densev.elastic.logging.annotations.LogResult)", returning = "result")
    public void logResultAdvice(JoinPoint joinPoint, Object result) throws Throwable {
        logResult(joinPoint, result);
    }

    @Around("@annotation(com.densev.elastic.logging.annotations.LogExecutionTime)")
    public Object logExecutionTimeAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return logExecutionTime(proceedingJoinPoint);
    }

    @Around("@annotation(com.densev.elastic.logging.annotations.LogAll)")
    public Object logAll(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logParams(proceedingJoinPoint);
        Object result = logExecutionTime(proceedingJoinPoint);
        logResult(proceedingJoinPoint, result);
        return result;
    }

    private void logParams(JoinPoint joinPoint) throws Throwable {
        LOG.debug("Method: {} called with args: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    private void logResult(JoinPoint joinPoint, Object result) throws Throwable {
        LOG.debug("Method: {} has returned result: {}", joinPoint.getSignature(), result != null ? result.toString() : null);
    }

    private Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object result = proceedingJoinPoint.proceed();
        stopwatch.stop();
        LOG.debug("Method: {} executed in {} ms", proceedingJoinPoint.getSignature(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }
}
