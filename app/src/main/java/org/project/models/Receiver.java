package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

//burası direkt kaydedilecek admin tarafından, receiver ya kişi ya da mağaza olacaktır o yüzden ikisinin de modeli
//oluşturuluyor, bu store içindeki çeşitli bilgilerile kaydedilecek ve order için entity olarak yazılacak
//order da cargo için entity olarak yazılacak
public class Receiver implements Identifiable {
    private Integer id;
    private String name,surname,address,email,password;

    public Receiver(String email, String password, String name, String surname, String address) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.password = password;
        System.out.println("receiver oluşturuldu");
    }
    @Override
    public Integer getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}

    public void setSurname(String surname) {this.surname = surname;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}


    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    /*
        @Override
    public String toString() {
        return "Receiver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
     */
}
