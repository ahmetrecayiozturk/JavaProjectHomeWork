package org.project.models;

import org.project.models.interfaces.Identifiable;


public class Cargo extends StoreItem implements Identifiable {
    private boolean isdelivered;
    private Integer orderId;
    private boolean isReturned;

    public Cargo(boolean isdelivered, Integer orderId) {
        super();
        this.isReturned = false;
        this.isdelivered = isdelivered;
        this.orderId = orderId;
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
}
