package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Product implements Identifiable {
    public Integer id;
    public String name;
    public String description;
    public int storeId;
    public String imageUrl;
    public Double price;
    public int productCount;

    public Product(String name, String description, int storeId, String imageUrl, Double price, int productCount) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.name = name;
        this.description = description;
        this.storeId = storeId;
        this.imageUrl = imageUrl;
        this.price = price;
        this.productCount=productCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public Product(){
        this.id = Math.abs(UUID.randomUUID().hashCode());

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
