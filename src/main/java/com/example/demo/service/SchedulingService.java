package com.example.demo.service;

import com.example.demo.config.IntegrationConfig;
import com.example.demo.jpa.entity.HumanEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SchedulingService {

    private static final int RATE_5_S = 5 * 1000;

    @Autowired private AsyncHumanService asyncHumanService;
    @Autowired private IntegrationConfig.Сonveyor сonveyor;

    @SneakyThrows
    @Scheduled(fixedRate = RATE_5_S)
    public void modify() {
        log.info("Modifying started");
        CompletableFuture<HumanEntity> modify = asyncHumanService.makeHuman();
        modify.whenComplete((s, throwable) -> {
            if (Objects.nonNull(throwable)) {
//                log.error(throwable.getMessage(), throwable);
            } else if (Objects.nonNull(s)) {
                log.info("We built a human with {} legs", s.getLegs().size());
                сonveyor.improveHuman(s);
            }
        });
        log.info("Modifying completed");
    }
}
