package com.example.demo.jpa.repository;

import com.example.demo.jpa.entity.HumanEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface HumanRepository extends Repository<HumanEntity, Integer> {

    HumanEntity save(HumanEntity humanEntity);

    List<HumanEntity> findAll();

    long count();
}
