package org.project.models;

import org.project.models.interfaces.Identifiable;


public class Order extends StoreItem implements Identifiable {
    private Integer productId;
    private Integer receiverId;
    private int quantity;
    private String status;

    public Order(Integer productId, Integer receiverId, int quantity) {
        super();
        this.productId = productId;
        this.receiverId = receiverId;
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", productId=" + productId +
                ", receiverId=" + receiverId +
                ", quantity=" + quantity +
                ", status=" + status +
                ", storeId=" + storeId;
    }
}