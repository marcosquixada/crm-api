package com.crm.api.controller.impl;

import com.crm.api.model.Customer;
import com.crm.api.payload.response.CustomerResponse;
import com.crm.api.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomerBaseController {

    static final Logger log = LoggerFactory.getLogger(CustomerControllerImpl.class);
    static final String ERROR_CUSTOMER_IS_NOT_FOUND = "Error: Customer not found.";

    @Value("${app.awsServices.simple.storage.url}")
    String s3Url;

    ResponseEntity<CustomerResponse> getCustomerResponseEntity(Customer customer) {
        CustomerResponse searchCustomerResponse = new CustomerResponse();

        searchCustomerResponse.setId(customer.getId());
        searchCustomerResponse.setName(customer.getName());
        searchCustomerResponse.setSurname(customer.getSurname());
        searchCustomerResponse.setPhotoUrl(customer.getPhotoUrl());
        searchCustomerResponse.setDeletedAt(customer.getDeletedAt());

        return new ResponseEntity<>(searchCustomerResponse, HttpStatus.OK);
    }

    UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails;
    }
}
