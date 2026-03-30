package com.arda.evrensesi.controller;

import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.request.LoginRequest;
import com.arda.evrensesi.request.RegisterRequest;
import com.arda.evrensesi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Auth", description = "Authentication operations")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists — user.already.exists",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed — e.g. passwords do not match, invalid email format",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Register request received email={}", registerRequest.email());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.userService.register(registerRequest));
    }

    @Operation(summary = "Login with email and password — session cookie is set on success")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, JSESSIONID cookie is set",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials — auth.bad.credentials or auth.user.not.found",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed — invalid email format or missing fields",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequest loginRequest,
                                         HttpServletRequest httpRequest,
                                         HttpServletResponse httpResponse) {
        log.info("Login request received email={}", loginRequest.email());
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.login(loginRequest, httpRequest, httpResponse));
    }
}