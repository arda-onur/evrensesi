package com.arda.evrensesi.request;

import com.arda.evrensesi.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Setter;

import java.util.Locale;


public record UserRequest(
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
        public UserRequest {
                if(email != null){
                 email = email.trim().toLowerCase(Locale.ROOT);
                }
        }
}
