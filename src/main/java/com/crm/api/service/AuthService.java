package com.crm.api.service;

import com.crm.api.model.User;

public interface AuthService {
    User save(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean userCanCreateCustomer(String username);
}
