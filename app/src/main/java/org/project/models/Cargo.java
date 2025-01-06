package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Cargo implements Identifiable {
    private Integer id;
    private boolean isDelivered;
    private boolean isReturned;
    private Integer orderId;

    public Cargo(boolean isDelivered, boolean isReturned, Integer orderId) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.isDelivered = isDelivered;
        this.isReturned = isReturned;
        this.orderId = orderId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
/*
package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Cargo implements Identifiable {
    private Integer id;
    private boolean isdelivered;
    private Integer orderId;

    public Cargo(boolean isdelivered, Integer orderId) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.isdelivered = isdelivered;
        this.orderId = orderId;
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

    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
*/