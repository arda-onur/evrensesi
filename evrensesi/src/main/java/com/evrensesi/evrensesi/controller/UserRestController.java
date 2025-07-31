package com.evrensesi.evrensesi.controller;

import com.evrensesi.evrensesi.dto.UserDto;
import com.evrensesi.evrensesi.mapper.UserMapper;
import com.evrensesi.evrensesi.request.UserRequest;
import com.evrensesi.evrensesi.service.UserCrudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private UserMapper userMapper;
    private UserCrudService userCrudService;
@PostMapping("/createuser")
    ResponseEntity<UserDto> createUser(@RequestBody @Valid UserRequest userRequest) {;
      return ResponseEntity.status(HttpStatus.CREATED)
              .body(this.userMapper.map(this.userCrudService.createUser(this.userMapper.map(userRequest))));
}

}
