package com.crm.api.payload.request;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

public class SaveCustomerRequest implements Serializable {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String photoUrl;

    @NotBlank
    private String username;

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return "SaveCustomerRequest{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
