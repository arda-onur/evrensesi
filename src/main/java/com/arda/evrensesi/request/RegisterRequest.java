package com.arda.evrensesi.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Locale;


public record RegisterRequest(
        @NotBlank(message = "user.request.validation.email")
        @Email
        String email,

        @NotBlank(message = "user.request.validation.password")
        @Size(min = 3, message ="user.request.validation.password.size" )
        String password,

        @NotBlank(message = "user.request.validation.password")
        @Size(min = 3, message ="user.request.validation.password.size" )
        String rePassword
)
{
        public RegisterRequest {
                if(email != null){
                 email = email.trim().toLowerCase(Locale.ROOT);
                }
        }
}
