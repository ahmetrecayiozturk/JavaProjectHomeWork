package org.project.models;

import org.project.App;
import org.project.data.Identifiable;

import java.util.UUID;

public class Cargo implements Identifiable {
    private Integer id;
    private boolean isdelivered;
    private Integer orderId;
    private Integer storeId;
    private boolean isReturned;

    public Cargo(boolean isdelivered, Integer orderId) {
        this.storeId= App.getCurrentStore().getId();
        this.isReturned = false;
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
    public Integer getStoreId() {
        return storeId;
    }
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}
