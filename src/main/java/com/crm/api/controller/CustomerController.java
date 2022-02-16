package com.crm.api.controller;

import com.crm.api.model.Customer;
import com.crm.api.payload.request.SaveCustomerRequest;
import com.crm.api.payload.request.UpdateCustomerRequest;
import com.crm.api.payload.response.CustomerResponse;
import com.crm.api.payload.response.MessageResponse;
import com.crm.api.security.services.UserDetailsImpl;
import com.crm.api.service.CustomerService;
import com.crm.api.service.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    public static final String ERROR_CUSTOMER_IS_NOT_FOUND = "Error: Customer not found.";

    StorageService storageService;
    CustomerService customerService;

    @Value("${app.awsServices.simple.storage.url")
    private String s3Url;


    public CustomerController(CustomerService customerService, StorageService storageService){
        this.customerService = customerService;
        this.storageService = storageService;
    }

    @GetMapping
    @Operation(summary = "List All Customers Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all customers")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Customer> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        log.info("Listing all customers: {}", "");
        Pageable pageable = PageRequest.of(page, size);
        return this.customerService.findByDeletedAtIsNull(pageable);
    }

    @PostMapping
    @Operation(summary = "Create Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
            String fileNameReturned = storageService.uploadFile(file);
            customer.setPhotoUrl(s3Url.concat(fileNameReturned));
        }

        Customer CustomerRetrieved = customerService.save(customer);

        return ResponseEntity.ok(new MessageResponse("Customer registered successfully!", CustomerRetrieved.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable(value = "id", required = true) String customerId){
        log.info("Listing customer: {}", customerId);
        UUID customerUuid = UUID.fromString(customerId);
        Optional<Customer> customerOpt = retrieveCustomer(customerUuid);
        Customer customer = customerOpt.get();

        return getCustomerResponseEntity(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<CustomerResponse> update(@PathVariable String id,
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
            String fileNameReturned = storageService.uploadFile(file);
            customer.setPhotoUrl(s3Url.concat(fileNameReturned));
        }

        customer = customerService.save(customer);
        return getCustomerResponseEntity(customer);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable(value = "id", required = true) String customerId) {
        log.info("Deleting customer: {}", customerId);
        UUID customerUuid = UUID.fromString(customerId);
        Optional<Customer> customerOpt = retrieveCustomer(customerUuid);
        Customer customer = customerOpt.get();

        UserDetailsImpl userDetails = getUserDetails();
        UUID userIdFromContext = userDetails.getId();

        customer.setUpdatedBy(userIdFromContext);
        customerService.delete(customer);
        return ResponseEntity.ok(new MessageResponse("Customer deleted successfully!"));
    }

    private Optional<Customer> retrieveCustomer(UUID id){
         return Optional.ofNullable(customerService.findById(id).orElseThrow(() -> new RuntimeException(ERROR_CUSTOMER_IS_NOT_FOUND)));
    }

    private ResponseEntity<CustomerResponse> getCustomerResponseEntity(Customer customer) {
        CustomerResponse searchCustomerResponse = new CustomerResponse();

        searchCustomerResponse.setId(customer.getId());
        searchCustomerResponse.setName(customer.getName());
        searchCustomerResponse.setSurname(customer.getSurname());
        searchCustomerResponse.setPhotoUrl(customer.getPhotoUrl());
        searchCustomerResponse.setDeletedAt(customer.getDeletedAt());

        return new ResponseEntity<>(searchCustomerResponse, HttpStatus.OK);
    }

    private UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails;
    }

}