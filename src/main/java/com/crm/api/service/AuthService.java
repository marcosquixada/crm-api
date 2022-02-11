package com.crm.api.service;

import com.crm.api.model.User;
import com.crm.api.repository.AuthRepository;
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
}
