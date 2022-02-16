package com.crm.api.controller.impl;

import com.crm.api.controller.CustomerController;
import com.crm.api.model.Customer;
import com.crm.api.payload.request.SaveCustomerRequest;
import com.crm.api.payload.response.CustomerResponse;
import com.crm.api.payload.response.MessageResponse;
import com.crm.api.security.services.UserDetailsImpl;
import com.crm.api.service.impl.CustomerServiceImpl;
import com.crm.api.service.impl.StorageServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.Converter;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CustomerControllerImpl extends CustomerBaseController implements CustomerController  {

    StorageServiceImpl storageServiceImpl;
    CustomerServiceImpl customerServiceImpl;

    public CustomerControllerImpl(CustomerServiceImpl customerServiceImpl, StorageServiceImpl storageServiceImpl){
        this.customerServiceImpl = customerServiceImpl;
        this.storageServiceImpl = storageServiceImpl;
    }

    @GetMapping
    @Operation(summary = "List All Customers Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all customers")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<CustomerResponse> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        log.info("Listing all customers: {}", "");
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> byDeletedAtIsNull = this.customerServiceImpl.findByDeletedAtIsNull(pageable);

        return byDeletedAtIsNull.map(CustomerResponse::new);
    }

    @Override
    public ResponseEntity<?> save(
            @Schema(description ="Add user")
            @Parameter(description = "Entry Param", required = true, example = "User")
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam("customerStr") String customerStr) throws JsonProcessingException {
        log.info("Saving customer: {}", customerStr);

        UserDetailsImpl userDetails = getUserDetails();

        UUID userIdFromContext = userDetails.getId();

        ObjectMapper mapper = new ObjectMapper();

        SaveCustomerRequest customerRequest = mapper.readValue(customerStr, SaveCustomerRequest.class);

        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setCreatedBy(userIdFromContext);

        if(file != null) {
            log.info("Uploading Image: {}", "");
            String fileNameReturned = storageServiceImpl.uploadFile(file);
            customer.setPhotoUrl(s3Url.concat(fileNameReturned));
        }

        Customer customerRetrieved = customerServiceImpl.save(customer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customerRetrieved.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable(value = "id", required = true) String customerId){
        log.info("Listing customer: {}", customerId);
        UUID customerUuid = UUID.fromString(customerId);
        Optional<Customer> customerOpt = retrieveCustomer(customerUuid);
        Customer customer = customerOpt.get();

        return getCustomerResponseEntity(customer);
    }

    @Override
    public ResponseEntity<CustomerResponse> update(@PathVariable String id,
                                            @RequestParam(value = "file") MultipartFile file,
                                            @RequestParam("customerStr") String customerStr) throws JsonProcessingException {
        log.info("Updating customer: {}", customerStr);

        UUID uuid = UUID.fromString(id);
        Optional<Customer> customerOpt = retrieveCustomer(uuid);
        Customer customer = customerOpt.get();

        UserDetailsImpl userDetails = getUserDetails();
        UUID userIdFromContext = userDetails.getId();

        customer.setUpdatedBy(userIdFromContext);

        ObjectMapper mapper = new ObjectMapper();

        SaveCustomerRequest customerRequest = mapper.readValue(customerStr, SaveCustomerRequest.class);

        customer.setName(customerRequest.getName() != null ? customerRequest.getName() : customer.getName());
        customer.setSurname(customerRequest.getSurname() != null ? customerRequest.getSurname() : customer.getSurname());

        if(file != null) {
            log.info("Uploading Image: {}", "");
            String fileNameReturned = storageServiceImpl.uploadFile(file);
            customer.setPhotoUrl(s3Url.concat(fileNameReturned));
        }

        customer = customerServiceImpl.save(customer);
        return getCustomerResponseEntity(customer);
    }

    @Override
    public ResponseEntity<?> delete(@PathVariable(value = "id", required = true) String customerId) {
        log.info("Deleting customer: {}", customerId);
        UUID customerUuid = UUID.fromString(customerId);
        Optional<Customer> customerOpt = retrieveCustomer(customerUuid);
        Customer customer = customerOpt.get();

        UserDetailsImpl userDetails = getUserDetails();
        UUID userIdFromContext = userDetails.getId();

        customer.setUpdatedBy(userIdFromContext);
        customerServiceImpl.delete(customer);
        return ResponseEntity.ok(new MessageResponse("Customer deleted successfully!"));
    }

    private Optional<Customer> retrieveCustomer(UUID id){
        return Optional.ofNullable(customerServiceImpl.findById(id).orElseThrow(() -> new RuntimeException(ERROR_CUSTOMER_IS_NOT_FOUND)));
    }

}