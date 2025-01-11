package org.project.models;

import org.project.models.interfaces.Nameable;
import org.project.models.interfaces.Describable;
import org.project.models.interfaces.Imageable;

public class Store extends StoreItem implements Nameable, Describable, Imageable {
    private String name;
    private String address;
    private String phone;
    private String description;
    private String imageUrl;

    public Store(String name, String address, String phone, String description, String imageUrl) {
        super();
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
