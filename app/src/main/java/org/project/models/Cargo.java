package org.project.models;

import org.project.App;
import org.project.data.Identifiable;

import java.util.UUID;

public class Cargo implements Identifiable {
    // Unique identifier for the cargo
    private Integer id;
    // Flag indicating whether the cargo is delivered
    private boolean isdelivered;
    // ID of the order associated with the cargo
    private Integer orderId;
    // ID of the store associated with the cargo
    private Integer storeId;
    // Flag indicating whether the cargo is returned
    private boolean isReturned;

    // Constructor to initialize a new Cargo object
    public Cargo(boolean isdelivered, boolean isReturned, Integer orderId) {
        // Set the store ID to the current store's ID
        this.storeId = App.getCurrentStore().getId();
        // Set the returned status
        this.isReturned = isReturned;
        // Generate a unique ID for the cargo
        this.id = Math.abs(UUID.randomUUID().hashCode());
        // Set the delivered status
        this.isdelivered = isdelivered;
        // Set the order ID
        this.orderId = orderId;
    }

    // Method to get the ID of the cargo
    @Override
    public Integer getId() {
        return id;
    }

    // Method to set the ID of the cargo
    public void setid(Integer id) {
        this.id = id;
    }

    // Method to check if the cargo is delivered
    public boolean isDelivered() {
        return isdelivered;
    }

    // Method to set the delivered status of the cargo
    public void setDelivered(boolean delivered) {
        isdelivered = delivered;
    }

    // Method to check if the cargo is returned
    public boolean isReturned() {
        return isReturned;
    }

    // Method to set the returned status of the cargo
    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    // Method to get the order ID associated with the cargo
    public Integer getOrderId() {
        return orderId;
    }

    // Method to set the order ID associated with the cargo
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    // Method to get the store ID associated with the cargo
    public Integer getStoreId() {
        return storeId;
    }

    // Method to set the store ID associated with the cargo
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}