package org.project.models;

import org.project.data.Identifiable;
import org.project.services.ProductService;

import java.util.UUID;

public class Order implements Identifiable {
    private Integer id;
    private Integer productId;
    private Integer receiverId;
    private int quantity;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order(Integer productId, Integer receiverId, int quantity) {
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
}