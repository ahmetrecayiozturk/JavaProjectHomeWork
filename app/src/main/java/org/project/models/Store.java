package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Store implements Identifiable {
    // Unique identifier for the store
    private Integer id;
    // Name of the store
    private String name;
    // Address of the store
    private String address;
    // Phone number of the store
    private String phone;
    // Description of the store
    private String description;
    // URL of the store image
    private String imageUrl;

    // Constructor to initialize a new Store object with specified details
    public Store(String name, String address, String phone, String description, String imageUrl) {
        // Generate a unique ID for the store
        this.id = Math.abs(UUID.randomUUID().hashCode());
        // Set the store name
        this.name = name;
        // Set the store address
        this.address = address;
        // Set the store phone number
        this.phone = phone;
        // Set the store description
        this.description = description;
        // Set the store image URL
        this.imageUrl = imageUrl;
    }

    // Method to get the image URL of the store
    public String getImageUrl() {
        return imageUrl;
    }

    // Method to set the image URL of the store
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Method to get the description of the store
    public String getDescription() {
        return description;
    }

    // Method to set the description of the store
    public void setDescription(String description) {
        this.description = description;
    }

    // Method to get the ID of the store
    @Override
    public Integer getId() {
        return id;
    }

    // Method to set the ID of the store
    public void setId(int id) {
        this.id = id;
    }

    // Method to get the name of the store
    public String getName() {
        return name;
    }

    // Method to set the name of the store
    public void setName(String name) {
        this.name = name;
    }

    // Method to get the address of the store
    public String getAddress() {
        return address;
    }

    // Method to set the address of the store
    public void setAddress(String address) {
        this.address = address;
    }

    // Method to get the phone number of the store
    public String getPhone() {
        return phone;
    }

    // Method to set the phone number of the store
    public void setPhone(String phone) {
        this.phone = phone;
    }
}