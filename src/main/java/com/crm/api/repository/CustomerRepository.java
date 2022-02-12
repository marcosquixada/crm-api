package com.crm.api.repository;

import com.crm.api.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, UUID> {

    Optional<Customer> findById(UUID userId);
    Page<Customer> findByDeletedAtIsNull(Pageable pageable);

}
