package com.exp.cl.user.business;

import com.exp.cl.user.models.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(String email, String password);
    void logout(String token);            
} 
