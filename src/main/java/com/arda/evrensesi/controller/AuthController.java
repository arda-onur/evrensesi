package com.arda.evrensesi.controller;


import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.request.UserRequest;
import com.arda.evrensesi.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRequest userRequest){
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(this.userServiceImpl.register(userRequest));
    }
}
