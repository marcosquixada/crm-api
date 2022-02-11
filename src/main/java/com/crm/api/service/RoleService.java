package com.crm.api.service;

import com.crm.api.model.ERole;
import com.crm.api.model.Role;
import com.crm.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByName(ERole roleUser) {
        return roleRepository.findByName(roleUser);
    }

    public boolean existsByName(ERole roleAdmin) {
        return roleRepository.existsByName(roleAdmin);
    }

    public void save(Role role) {
        roleRepository.save(role);
    }
}
