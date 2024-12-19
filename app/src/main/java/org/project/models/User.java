package org.project.models;

import org.project.data.Identifiable;

import java.util.UUID;

public class User implements Identifiable {
    private Integer id;
    private String email;
    private String password;

    public User(String email, String password) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getId() {
        return id;
    }
}