package com.evrensesi.evrensesi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
