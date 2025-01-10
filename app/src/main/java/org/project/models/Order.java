package org.project.models;

import org.project.App;
import org.project.data.Identifiable;
import org.project.services.ProductService;

import java.util.UUID;

public class Order implements Identifiable {
    private Integer id;
    private Integer productId;
    private Integer receiverId;
    private int quantity;
    private String status;
    private Integer storeId;

    public Order(Integer productId, Integer receiverId, int quantity) {
        this.storeId= App.getCurrentStore().getId();
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.productId = productId;
        this.receiverId = receiverId;
        this.quantity = quantity;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public Integer getStoreId() {
        return storeId;
    }
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
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
                ", status='" + status + '\'' +
                ", storeId=" + storeId ;
    }
}