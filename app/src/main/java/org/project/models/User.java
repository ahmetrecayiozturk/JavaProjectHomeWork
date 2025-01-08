package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class User implements Identifiable {
    // Unique identifier for the user
    private Integer id;
    // Email of the user
    private String email;
    // Password of the user
    private String password;
    // ID of the store associated with the user
    private int storeId;

    // Constructor to initialize a new User object with specified details
    public User(String email, String password, int storeId) {
        // Generate a unique ID for the user
        this.id = Math.abs(UUID.randomUUID().hashCode());
        // Set the user's email
        this.email = email;
        // Set the user's password
        this.password = password;
        // Set the store ID associated with the user
        this.storeId = storeId;
    }

    // Method to get the store ID associated with the user
    public int getStoreId() {
        return storeId;
    }

    // Method to set the store ID associated with the user
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    // Method to get the email of the user
    public String getEmail() {
        return email;
    }

    // Method to set the email of the user
    public void setEmail(String email) {
        this.email = email;
    }

    // Method to get the password of the user
    public String getPassword() {
        return password;
    }

    // Method to set the password of the user
    public void setPassword(String password) {
        this.password = password;
    }

    // Method to get the ID of the user
    @Override
    public Integer getId() {
        return id;
    }
}