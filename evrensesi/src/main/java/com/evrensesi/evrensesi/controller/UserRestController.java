package com.evrensesi.evrensesi.controller;

import com.evrensesi.evrensesi.dto.UserDto;
import com.evrensesi.evrensesi.mapper.UserMapper;
import com.evrensesi.evrensesi.model.User;
import com.evrensesi.evrensesi.request.UserRequest;
import com.evrensesi.evrensesi.service.UserCrudService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")

public class UserRestController {

    private final UserMapper userMapper;
    private final UserCrudService userCrudService;

    public UserRestController(UserMapper userMapper, UserCrudService userCrudService) {
        this.userMapper = userMapper;
        this.userCrudService = userCrudService;
    }


    @PostMapping("/createuser")
    ResponseEntity<UserDto> createUser(@RequestBody @Valid UserRequest userRequest) {;
      return ResponseEntity.status(HttpStatus.CREATED)
              .body(this.userMapper.map(this.userCrudService.createUser(this.userMapper.map(userRequest))));
}

    @PostMapping("/login")
     ResponseEntity<UserDto> authenticateUser(@RequestBody @Valid UserRequest userRequest,HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userMapper.map(this.userCrudService.authenticateUser(userRequest ,response)));
     }

}
