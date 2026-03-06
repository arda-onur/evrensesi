package com.arda.evrensesi.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "user.request.validation.email")
        @Email
        String email,

        @NotBlank(message = "user.request.validation.password")
        @Size(min = 3, message ="user.request.validation.password.size" )
        String password
){
}
