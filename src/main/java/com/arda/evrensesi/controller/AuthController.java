package com.arda.evrensesi.controller;


import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.request.LoginRequest;
import com.arda.evrensesi.request.RegisterRequest;
import com.arda.evrensesi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest){
        log.info("Register request received email={}", registerRequest.email());
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(this.userService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest,
                                      HttpServletResponse httpResponse){
        log.info("Login request received email={}", loginRequest.email());
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.login(loginRequest,httpRequest,httpResponse));
    }
}
