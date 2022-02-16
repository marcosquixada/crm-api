package com.crm.api.service.impl;

import com.crm.api.model.Customer;
import com.crm.api.repository.CustomerRepository;
import com.crm.api.service.CustomerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private AuthServiceImpl authServiceImpl;
    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value="customer", allEntries=true),
            @CacheEvict(value="customers", allEntries=true)})
    public Customer save(Customer customer) {
        return this.customerRepository.save(customer);
    }

    @Override
    @Cacheable("customers")
    public Page<Customer> findAll(Pageable pageable) {
        return this.customerRepository.findAll(pageable);
    }

    @Override
    @Cacheable("customers")
    public Page<Customer> findByDeletedAtIsNull(Pageable pageable) {
        return this.customerRepository.findByDeletedAtIsNull(pageable);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value="customer", allEntries=true),
            @CacheEvict(value="customers", allEntries=true)})
    public void delete(Customer customer){
        customer.setDeletedAt(new Timestamp(System.currentTimeMillis()));
        this.customerRepository.save(customer);
    }

    @Override
    @Cacheable("customer")
    public Optional<Customer> findById(UUID id) {
        System.out.println("Entrou");
        return this.customerRepository.findById(id);
    }

}
