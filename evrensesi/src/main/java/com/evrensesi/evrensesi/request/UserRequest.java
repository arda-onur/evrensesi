package com.evrensesi.evrensesi.request;

import com.evrensesi.evrensesi.utility.regex.EmailRegexPattern;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "{create.user.request.username.not.blank.message}")
        @Size(message = "{create.user.request.username.size.message}", min = 4, max = 255)
        String username,
        @NotBlank(message = "{create.user.request.password.not.blank.message}")
        @Size(message = "{create.user.request.password.size.message}", min = 4, max = 255)
        String password,
        @NotBlank(message = "{create.user.request.confirmPassword.not.blank.message}")
        @Size(message = "{create.user.request.confirmPassword.size.message}", min = 4, max = 255)
        String confirmPassword,
        @NotBlank(message = "{create.user.request.email.not.blank.message}")
        @Email(regexp = EmailRegexPattern.EMAIL_PATTERN)
        String email) {

        @AssertTrue(message = "{create.user.request.dont.match.password.message}")
        public boolean isPasswordsMatch() {
                return password != null && password.equals(confirmPassword);
        }

}
