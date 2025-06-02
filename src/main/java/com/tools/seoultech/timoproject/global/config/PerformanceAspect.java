package com.tools.seoultech.timoproject.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    @Pointcut("@annotation(com.tools.seoultech.timoproject.global.annotation.PerformanceTimer)")
    private void performanceTimer() {}

    @Around("performanceTimer()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed(); // 실제 메서드 실행
            return result;
        } finally {
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getTotalTimeMillis();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getMethod().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();

            log.info("Performance Measurement - Class: {}, Method: {}, Execution Time: {}ms",
                    className, methodName, totalTimeMillis);
        }
    }
}