package com.crm.api.controller;

import com.crm.api.payload.request.LogOutRequest;
import com.crm.api.payload.request.LoginRequest;
import com.crm.api.payload.request.SignupRequest;
import com.crm.api.payload.request.TokenRefreshRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface AuthController {

    @PostMapping("/logout")
    ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest);

    @PostMapping("/refreshtoken")
    ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request);

    @PostMapping("/signin")
    @Operation(summary = "User Authentication")
    ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);


    @PostMapping("/signup")
    @Operation(summary = "Save User Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save a user")
    })
    ResponseEntity<?> save( @Schema(description ="Add user")
                            @Parameter(description = "Entry Param", required = true, example = "User")
                            @Valid @RequestBody SignupRequest signupRequest);
}
