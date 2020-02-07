package com.example.demo.aspects;

import com.example.demo.jpa.entity.HumanEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class HumanAspect {

    @Around("@annotation(com.example.demo.aspects.LogResult)")
    public Object log(ProceedingJoinPoint jp) throws Throwable {
        final Object proceed = jp.proceed();
        if (Objects.equals(CompletableFuture.class, proceed.getClass())) {
            CompletableFuture<HumanEntity> future = (CompletableFuture<HumanEntity>) proceed;
            future.handle((humanEntity, throwable) -> {
                if (Objects.nonNull(throwable)) {
                    log.error(throwable.getMessage(), throwable);
                    return throwable;
                }
                log.info("Human with {} legs catched", humanEntity.getLegs().size());
                return humanEntity;
            });
        }
        return proceed;
    }
}
