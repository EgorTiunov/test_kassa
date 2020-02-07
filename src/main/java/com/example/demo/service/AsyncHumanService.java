package com.example.demo.service;

import com.example.demo.aspects.LogResult;
import com.example.demo.jpa.entity.HumanEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
public class AsyncHumanService {

    @Autowired private HumanService humanService;

    @LogResult
    @Async
    public CompletableFuture<HumanEntity> makeHuman() {
        if (humanService.count() > 10) return CompletableFuture.completedFuture(null);
        HumanEntity humanEntity = humanService.makeHuman();
        if (humanEntity.getLegs().size() % 2 != 0) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    throw new RuntimeException("Not even");
                } catch (RuntimeException ex) {
                    throw new CompletionException(ex);
                }
            });
        }
        return CompletableFuture.supplyAsync(() -> humanEntity);
    }
}
