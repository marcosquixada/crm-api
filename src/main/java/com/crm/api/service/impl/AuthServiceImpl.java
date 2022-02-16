package com.crm.api.service.impl;

import com.crm.api.model.ERole;
import com.crm.api.model.User;
import com.crm.api.repository.AuthRepository;
import com.crm.api.security.services.UserDetailsServiceImpl;
import com.crm.api.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthRepository authRepository;

    public AuthServiceImpl(AuthRepository authRepository){
        this.authRepository = authRepository;
    }

    @Override
    public User save(User user){
        return authRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username){
        return authRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email){
        return  authRepository.existsByEmail(email);
    }

    @Override
    public boolean userCanCreateCustomer(String username){
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(authRepository);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails.getAuthorities().contains(ERole.ROLE_ADMIN);
    }
}
