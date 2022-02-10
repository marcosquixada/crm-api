package com.crm.api.controller;

import com.crm.api.model.User;
import com.crm.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;



@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    @Operation(summary = "Save User Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save a user")
    })
    public User save( @Schema(description ="Add user") @Parameter(description = "Entry Param", required = true, example = "User") @Valid @RequestBody User user){
        return userService.save(user);
    }
}
