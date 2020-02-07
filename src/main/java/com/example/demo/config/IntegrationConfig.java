package com.example.demo.config;

import com.example.demo.jpa.entity.HumanEntity;
import com.example.demo.jpa.entity.LegEntity;
import com.example.demo.service.HumanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class IntegrationConfig {

    @MessagingGateway
    public interface Сonveyor {

        @Gateway(requestChannel = "humans.input")
        void improveHuman(HumanEntity human);

    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedDelay(1000).get();
    }

    @Bean
    public IntegrationFlow humans(
            PlatformTransactionManager platformTransactionManager,
            EntityManagerFactory entityManagerFactory
    ) {
        return f -> f
                .channel(c -> c.executor(Executors.newCachedThreadPool()))
                .filter(HumanEntity.class, filter())
                .<HumanEntity, HumanEntity>transform(transform())
                .handle(
                        Jpa.outboundAdapter(entityManagerFactory).entityClass(HumanEntity.class).persistMode(PersistMode.MERGE),
                        e -> e.transactional(platformTransactionManager)
                );
    }

    private GenericSelector<HumanEntity> filter() {
        return humanEntity -> {
            log.info("Legs {}", humanEntity.getLegs().size());
            try {
                return humanEntity.getLegs().size() != 2;
            } catch (Exception e) {
                return false;
            }
        };
    }

    private GenericTransformer<HumanEntity, HumanEntity> transform() {
        return human -> {
            int size = human.getLegs().size();
            if (size > 2) {
                log.info("Crop legs");
                List<LegEntity> legEntities = new ArrayList<>(human.getLegs().subList(0, 2));
                human.getLegs().clear();
                human.setLegs(legEntities);
            } else if (size == 1) {
                log.info("Add one leg");
                human.getLegs().add(HumanService.makeLeg(human));
            } else {
                log.info("Add two legs");
                human.getLegs().add(HumanService.makeLeg(human));
                human.getLegs().add(HumanService.makeLeg(human));
            }
            return human;
        };
    }
}
