package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

//burası direkt kaydedilecek admin tarafından, receiver ya kişi ya da mağaza olacaktır o yüzden ikisinin de modeli
//oluşturuluyor, bu store içindeki çeşitli bilgilerile kaydedilecek ve order için entity olarak yazılacak
//order da cargo için entity olarak yazılacak
public class Store implements Identifiable {
    private Integer id;
    private String name,address,phone;
    private String description;
    private String imageUrl;
    public Store(String name, String address, String phone,String description,String imageUrl) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
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

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
