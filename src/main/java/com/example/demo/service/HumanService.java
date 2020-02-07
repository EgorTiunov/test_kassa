package com.example.demo.service;

import com.example.demo.jpa.entity.HumanEntity;
import com.example.demo.jpa.entity.LegEntity;
import com.example.demo.jpa.repository.HumanRepository;
import com.example.demo.types.Side;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class HumanService {
    private static final Random RANDOM = new SecureRandom();

    @Autowired private HumanRepository humanRepository;

    @Transactional(readOnly = true)
    public long count() {
        return humanRepository.count();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public HumanEntity makeHuman() {
        HumanEntity human = new HumanEntity();
        human.setName("Human");
        human.setLegs(makeLegs(human));
        return humanRepository.save(human);
    }

    private List<LegEntity> makeLegs(HumanEntity human) {
        int i = RANDOM.nextInt(10);
        return IntStream.range(0, i).mapToObj(j -> makeLeg(human)).collect(Collectors.toList());
    }

    public static LegEntity makeLeg(HumanEntity human) {
        int k = RANDOM.nextInt(10);
        LegEntity legEntity = new LegEntity();
        legEntity.setSide(k >= 5 ? Side.LEFT : Side.RIGHT);
        legEntity.setHuman(human);
        return legEntity;
    }
}
