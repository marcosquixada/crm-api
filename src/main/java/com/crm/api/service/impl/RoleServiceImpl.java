package com.crm.api.service.impl;

import com.crm.api.model.ERole;
import com.crm.api.model.Role;
import com.crm.api.repository.RoleRepository;
import com.crm.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(ERole roleUser) {
        return roleRepository.findByName(roleUser);
    }

    @Override
    public boolean existsByName(ERole roleAdmin) {
        return roleRepository.existsByName(roleAdmin);
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }
}
