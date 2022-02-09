package com.crm.api.controller;

import com.crm.api.model.User;
import com.crm.api.service.UserService;
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
    public User save(@Valid @RequestBody User user){
        //User user = new User("marcos", "marcos@marcos.com", "123456");
        return userService.save(user);
    }
}
