package com.kdd.productshop.domain.entities;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    private Integer id;

    public BaseEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
