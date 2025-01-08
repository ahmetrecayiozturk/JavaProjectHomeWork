package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Order implements Identifiable {
    // Unique identifier for the order
    private Integer id;
    // ID of the product associated with the order
    private Integer productId;
    // ID of the receiver associated with the order
    private Integer receiverId;
    // Quantity of the product in the order
    private int quantity;
    // Status of the order
    private String status;
    // ID of the store associated with the order
    private Integer storeId;

    // Constructor to initialize a new Order object
    public Order(Integer productId, Integer receiverId, int quantity) {
        // Generate a unique ID for the order
        this.id = Math.abs(UUID.randomUUID().hashCode());
        // Set the product ID
        this.productId = productId;
        // Set the receiver ID
        this.receiverId = receiverId;
        // Set the quantity
        this.quantity = quantity;
    }

    // Method to get the ID of the order
    @Override
    public Integer getId() {
        return id;
    }

    // Method to set the ID of the order
    public void setId(Integer id) {
        this.id = id;
    }

    // Method to get the product ID associated with the order
    public Integer getProductId() {
        return productId;
    }

    // Method to set the product ID associated with the order
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    // Method to get the receiver ID associated with the order
    public Integer getReceiverId() {
        return receiverId;
    }

    // Method to set the receiver ID associated with the order
    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    // Method to get the quantity of the product in the order
    public int getQuantity() {
        return quantity;
    }

    // Method to set the quantity of the product in the order
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to get the status of the order
    public String getStatus() {
        return status;
    }

    // Method to set the status of the order
    public void setStatus(String status) {
        this.status = status;
    }

    // Method to get the store ID associated with the order
    public Integer getStoreId() {
        return storeId;
    }

    // Method to set the store ID associated with the order
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}