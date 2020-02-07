package com.example.demo.jpa.entity;

import com.example.demo.types.Side;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_leg")
public class LegEntity implements Serializable {
    private Integer id;
    private Side side;
    private HumanEntity human;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    @ManyToOne
    @JoinColumn(name = "id_human")
    public HumanEntity getHuman() {
        return human;
    }

    public void setHuman(HumanEntity human) {
        this.human = human;
    }
}
