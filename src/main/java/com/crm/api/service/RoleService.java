package com.crm.api.service;

import com.crm.api.model.ERole;
import com.crm.api.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(ERole roleUser);
    boolean existsByName(ERole roleAdmin);
    void save(Role role);
}
