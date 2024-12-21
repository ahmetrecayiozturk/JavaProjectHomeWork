package org.project.models;

import java.util.UUID;

//burası direkt kaydedilecek admin tarafından, receiver ya kişi ya da mağaza olacaktır o yüzden ikisinin de modeli
//oluşturuluyor, bu store içindeki çeşitli bilgilerile kaydedilecek ve order için entity olarak yazılacak
//order da cargo için entity olarak yazılacak
public class Receiver extends User {
    private Integer id;
    private String name,surname,address;
    public Receiver(Integer id,String email, String password, String name, String surname, String address) {
        super(email, password);
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.name = name;
        this.surname = surname;
        this.address = address;
    }

    public Integer getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}

    public void setSurname(String surname) {this.surname = surname;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}
}
