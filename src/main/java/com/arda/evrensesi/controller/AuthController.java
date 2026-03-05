package com.arda.evrensesi.controller;


import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.request.UserRequest;
import com.arda.evrensesi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRequest userRequest){
        log.info("Register request received email={}", userRequest.email());
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(this.userServiceImpl.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserRequest userRequest, HttpServletRequest httpRequest){
        log.info("Login request received email={}", userRequest.email());
        this.userServiceImpl.login(userRequest,httpRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
