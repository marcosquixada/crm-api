package com.crm.api.payload.response;

import com.crm.api.model.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

public class CustomerResponse {
    private UUID id;
    private String name;
    private String surname;
    private String photoUrl;

    @JsonInclude(Include.NON_NULL)
    private Timestamp deletedAt;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.surname = customer.getSurname();
        this.photoUrl = customer.getPhotoUrl();
    }

    @Deprecated
    public CustomerResponse() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }
}
