package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

//bu cargonun entitysi order olacaktır
public class Cargo<T> implements Identifiable {
    private Integer id;
    private boolean isdelivered;
    private T entity;

    public Cargo(boolean isdelivered, T entity) {
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

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

}
