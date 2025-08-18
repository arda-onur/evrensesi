package com.evrensesi.evrensesi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String confirmPassword;
    private String email;
}
