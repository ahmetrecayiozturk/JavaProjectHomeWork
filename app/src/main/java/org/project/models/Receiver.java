package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class Receiver implements Identifiable {
    // Unique identifier for the receiver
    private Integer id;
    // Name of the receiver
    private String name;
    // Surname of the receiver
    private String surname;
    // Address of the receiver
    private String address;
    // Email of the receiver
    private String email;
    // Password of the receiver
    private String password;

    // Constructor to initialize a new Receiver object with specified details
    public Receiver(String email, String password, String name, String surname, String address) {
        // Generate a unique ID for the receiver
        this.id = Math.abs(UUID.randomUUID().hashCode());
        // Set the receiver's name
        this.name = name;
        // Set the receiver's surname
        this.surname = surname;
        // Set the receiver's address
        this.address = address;
        // Set the receiver's email
        this.email = email;
        // Set the receiver's password
        this.password = password;
    }

    // Method to get the ID of the receiver
    @Override
    public Integer getId() {
        return id;
    }

    // Method to set the ID of the receiver
    public void setId(int id) {
        this.id = id;
    }

    // Method to get the name of the receiver
    public String getName() {
        return name;
    }

    // Method to set the name of the receiver
    public void setName(String name) {
        this.name = name;
    }

    // Method to get the surname of the receiver
    public String getSurname() {
        return surname;
    }

    // Method to set the surname of the receiver
    public void setSurname(String surname) {
        this.surname = surname;
    }

    // Method to get the address of the receiver
    public String getAddress() {
        return address;
    }

    // Method to set the address of the receiver
    public void setAddress(String address) {
        this.address = address;
    }

    // Method to get the email of the receiver
    public String getEmail() {
        return email;
    }

    // Method to set the email of the receiver
    public void setEmail(String email) {
        this.email = email;
    }

    // Method to get the password of the receiver
    public String getPassword() {
        return password;
    }

    // Method to set the password of the receiver
    public void setPassword(String password) {
        this.password = password;
    }

    // Method to return a string representation of the receiver
    @Override
    public String toString() {
        return "Receiver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}