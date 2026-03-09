package com.arda.evrensesi.UnitTest;

import com.arda.evrensesi.entity.User;
import com.arda.evrensesi.exception.customException.UserRegistrationException;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.LoginRequest;
import com.arda.evrensesi.request.RegisterRequest;
import com.arda.evrensesi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpSession httpSession;

    @Mock
    Authentication authentication;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void register_shouldThrowException_whenEmailExists() {
        String email = "test@mail.com";
        RegisterRequest request = new RegisterRequest(email, "123", "123");

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserRegistrationException.class, () -> userService.register(request));

        verify(userRepository).existsByEmail(email);
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowException_whenPasswordsMismatch() {
        RegisterRequest request = new RegisterRequest("test@mail.com", "123", "456");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);

        assertThrows(UserRegistrationException.class, () -> userService.register(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_shouldSaveUser() {
        RegisterRequest request = new RegisterRequest("test@mail.com", "123", "123");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded");

        userService.register(request);

        verify(passwordEncoder).encode("123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_shouldAuthenticateSuccessfully() {
        LoginRequest request = new LoginRequest("test@mail.com", "123");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);

        userService.login(request, httpServletRequest);

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_shouldStoreSecurityContextInSession() {
        LoginRequest request = new LoginRequest("test@mail.com", "123");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(httpServletRequest.getSession(true)).thenReturn(httpSession);

        userService.login(request, httpServletRequest);

        verify(httpSession).setAttribute(anyString(), any());
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreWrong() {
        LoginRequest request = new LoginRequest("test@mail.com", "123");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad Credentials"));

        assertThrows(BadCredentialsException.class,
                () -> userService.login(request, httpServletRequest));

        verify(httpServletRequest, never()).getSession(true);
    }
}