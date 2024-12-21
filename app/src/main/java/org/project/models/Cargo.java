package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

//bu cargonun entitysi order olacaktır
public class Cargo<T> implements Identifiable {
    private int id;
    private String cargocompanyname;
    private boolean isdelivered;
    private T entity;

    public Cargo(int id, String cargocompanyname, boolean isdelivered, T entity) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.cargocompanyname = cargocompanyname;
        this.isdelivered = isdelivered;
        this.entity = entity;
    }
    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getCargocompanyname() {
        return cargocompanyname;
    }

    public void setCargocompanyname(String cargocompanyname) {
        this.cargocompanyname = cargocompanyname;
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
    @Override
    public Integer getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }
}
