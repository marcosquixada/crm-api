package com.crm.api.service;

import com.crm.api.model.ERole;
import com.crm.api.model.User;
import com.crm.api.repository.AuthRepository;
import com.crm.api.security.services.UserDetailsServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private AuthRepository authRepository;

    public AuthService(AuthRepository authRepository){
        this.authRepository = authRepository;
    }

    public User save(User user){
        return authRepository.save(user);
    }

    public boolean existsByUsername(String username){
        return authRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return  authRepository.existsByEmail(email);
    }

    public boolean userCanCreateCustomer(String username){
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(authRepository);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails.getAuthorities().contains(ERole.ROLE_ADMIN);
    }
}
