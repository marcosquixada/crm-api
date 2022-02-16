package com.crm.api.service;

import com.crm.api.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

   Customer save(Customer customer);
   Page<Customer> findAll(Pageable pageable);
   Page<Customer> findByDeletedAtIsNull(Pageable pageable);
   void delete(Customer customer);
   Optional<Customer> findById(UUID id);

}
