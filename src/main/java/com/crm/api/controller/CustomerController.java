package com.crm.api.controller;

import com.crm.api.payload.response.CustomerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/customers")
public interface CustomerController {

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<CustomerResponse> getCustomerById(@PathVariable(value = "id", required = true) String customerId);

    @PostMapping
    @Operation(summary = "Create Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<?> save(
            @Schema(description ="Add user")
            @Parameter(description = "Entry Param", required = true, example = "User")
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam("customerStr") String customerStr) throws JsonProcessingException;

    @PutMapping("/{id}")
    @Operation(summary = "Update Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<CustomerResponse> update(@PathVariable String id,
                                            @RequestParam(value = "file") MultipartFile file,
                                            @RequestParam("customerStr") String customerStr) throws JsonProcessingException;



    @DeleteMapping("{id}")
    @Operation(summary = "Delete Customer Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete a customer")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    ResponseEntity<?> delete(@PathVariable(value = "id", required = true) String customerId);
}
