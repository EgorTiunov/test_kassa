package com.example.demo.jpa.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_human")
public class HumanEntity implements Serializable {
    private Integer id;
    private String name;
    private List<LegEntity> legs = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "human", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<LegEntity> getLegs() {
        return legs;
    }

    public void setLegs(List<LegEntity> legs) {
        this.legs = legs;
    }
}
