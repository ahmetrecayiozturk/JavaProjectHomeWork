package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Order<T1, T2> implements Identifiable {
    private Integer id;
    private T1 entity1;
    private T2 entity2;

    public Order(T1 entity1, T2 entity2) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T1 getEntity1() {
        return entity1;
    }

    public void setEntity1(T1 entity) {
        this.entity1 = entity;
    }

    public T2 getEntity2() {
        return entity2;
    }

    public void setEntity2(T2 entity2) {
        this.entity2 = entity2;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", entity1=" + entity1 +
                ", entity2=" + entity2 +
                '}';
    }
}

