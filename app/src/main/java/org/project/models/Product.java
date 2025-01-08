package org.project.models;

import org.project.App;
import org.project.data.Identifiable;

import java.util.UUID;

public class Product implements Identifiable {
    // Unique identifier for the product
    public Integer id;
    // Name of the product
    public String name;
    // Description of the product
    public String description;
    // ID of the store associated with the product
    public int storeId;
    // URL of the product image
    public String imageUrl;
    // Price of the product
    public Double price;
    // Count of the product available in stock
    public int productCount;

    // Constructor to initialize a new Product object with specified details
    public Product(String name, String description, int storeId, String imageUrl, Double price, int productCount) {
        // Set the store ID to the current store's ID
        this.storeId = App.getCurrentStore().getId();
        // Generate a unique ID for the product
        this.id = Math.abs(UUID.randomUUID().hashCode());
        // Set the product name
        this.name = name;
        // Set the product description
        this.description = description;
        // Set the store ID
        this.storeId = storeId;
        // Set the image URL
        this.imageUrl = imageUrl;
        // Set the product price
        this.price = price;
        // Set the product count
        this.productCount = productCount;
    }

    // Default constructor to initialize a new Product object with a unique ID
    public Product() {
        // Generate a unique ID for the product
        this.id = Math.abs(UUID.randomUUID().hashCode());
    }

    // Method to get the ID of the product
    @Override
    public Integer getId() {
        return id;
    }

    // Method to set the ID of the product
    public void setId(Integer id) {
        this.id = id;
    }

    // Method to get the name of the product
    public String getName() {
        return name;
    }

    // Method to set the name of the product
    public void setName(String name) {
        this.name = name;
    }

    // Method to get the description of the product
    public String getDescription() {
        return description;
    }

    // Method to set the description of the product
    public void setDescription(String description) {
        this.description = description;
    }

    // Method to get the store ID associated with the product
    public int getStoreId() {
        return storeId;
    }

    // Method to set the store ID associated with the product
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    // Method to get the image URL of the product
    public String getImageUrl() {
        return imageUrl;
    }

    // Method to set the image URL of the product
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Method to get the price of the product
    public Double getPrice() {
        return price;
    }

    // Method to set the price of the product
    public void setPrice(Double price) {
        this.price = price;
    }

    // Method to get the count of the product available in stock
    public int getProductCount() {
        return productCount;
    }

    // Method to set the count of the product available in stock
    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
    public String getInfo(){
        return this.name + " " + this.description + " " + this.storeId;
    }
}