package com.arda.evrensesi.service;

import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.request.LoginRequest;
import com.arda.evrensesi.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;



public interface UserService {
    UserDTO register(RegisterRequest registerRequest);
    void login(LoginRequest registerRequest, HttpServletRequest httpRequest);
}
