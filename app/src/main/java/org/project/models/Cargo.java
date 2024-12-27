package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Cargo<T> implements Identifiable {
    private Integer id;
    private boolean isdelivered;
    private Order entity;

    public Cargo(boolean isdelivered, Order entity) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.isdelivered = isdelivered;
        this.entity = entity;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setid(Integer id) {
        this.id = id;
    }

    public boolean isDelivered() {
        return isdelivered;
    }

    public void setDelivered(boolean delivered) {
        isdelivered = delivered;
    }

    public Order getEntity() {
        return entity;
    }

    public void setEntity(Order entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", isdelivered=" + isdelivered +
                ", entity=" + entity +
                '}';
    }
}