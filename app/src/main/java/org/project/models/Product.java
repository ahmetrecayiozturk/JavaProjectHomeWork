package org.project.models;

import org.project.models.interfaces.Nameable;
import org.project.models.interfaces.Describable;
import org.project.models.interfaces.Imageable;
import org.project.models.interfaces.Priceable;

public class Product extends StoreItem implements Nameable, Describable, Imageable, Priceable {
    private String name;
    private String description;
    private String imageUrl;
    private Double price;
    private int productCount;

    public Product(String name, String description, int storeId, String imageUrl, Double price, int productCount) {
        super();
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.productCount = productCount;
    }

    public Product() {
        super();
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
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

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", storeId='" + storeId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price='" + price + '\'' +
                ", productCount='" + productCount ;
    }
}
