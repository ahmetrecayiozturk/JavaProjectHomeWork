package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Receiver implements Identifiable {
    private Integer id;
    private String name;
    private String surname;
    private String address;
    private String email;

    public Receiver(String email, String name, String surname, String address) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email ;
    }
}
