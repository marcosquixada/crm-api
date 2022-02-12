package com.crm.api.service;

import com.crm.api.model.Customer;
import com.crm.api.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private AuthService authService;
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return this.customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return this.customerRepository.save(customer);
    }

    public Page<Customer> findAll(Pageable pageable) {
        return this.customerRepository.findAll(pageable);
    }

    public Page<Customer> findByDeletedAtIsNull(Pageable pageable) {
        return this.customerRepository.findByDeletedAtIsNull(pageable);
    }

    public void delete(Customer customer){
        customer.setDeletedAt(new Timestamp(System.currentTimeMillis()));
        this.customerRepository.save(customer);

    }

    public Optional<Customer> findById(UUID id) {
        return this.customerRepository.findById(id);
    }

}
