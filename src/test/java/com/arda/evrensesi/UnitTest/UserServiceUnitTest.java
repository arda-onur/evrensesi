package com.arda.evrensesi.UnitTest;

import com.arda.evrensesi.exception.customException.UserRegistrationException;
import com.arda.evrensesi.model.entity.User;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.LoginRequest;
import com.arda.evrensesi.request.RegisterRequest;
import com.arda.evrensesi.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private HttpSessionSecurityContextRepository securityContextRepository;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @InjectMocks
    UserServiceImpl userService;
    static RegisterRequest normalRequest;
    static RegisterRequest notMatchPassRequest;
    static LoginRequest normalLoginRequest;

    @BeforeAll
    public static void setUser(){
               normalRequest = new RegisterRequest(
                "arda@gmail.com",
                "12345",
                "12345");
               notMatchPassRequest = new RegisterRequest(
                "arda@gmail.com",
                "12345",
                "1234");
               normalLoginRequest =  new LoginRequest(
                       "arda@mail.com",
                       "123456");
    }

    @Test
    public void register_shouldRegisterUserSuccessfully(){
    when(userRepository.existsByEmail(normalRequest.email())).thenReturn(false);
    when(passwordEncoder.encode(normalRequest.password())).thenReturn("encodedPassword");

    userService.register(normalRequest);

    verify(userRepository).save(any(User.class));
    }
    @Test
    public void register_shouldThrowExceptionWhenEmailAlreadyExists(){
        when(userRepository.existsByEmail(normalRequest.email())).thenReturn(true);

        assertThrows(UserRegistrationException.class, () -> userService.register(normalRequest));

        verify(userRepository,never()).save(any());
    }

    @Test
    public void register_shouldThrowExceptionWhenPasswordsDoNotMatch(){
        when(userRepository.existsByEmail(notMatchPassRequest.email())).thenReturn(false);

        assertThrows(UserRegistrationException.class,()-> userService.register(notMatchPassRequest));

        verify(userRepository,never()).save(any());
    }
    @Test
    public void register_shouldSaveUserWithEncodedPassword(){
        when(userRepository.existsByEmail(normalRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(normalRequest.password())).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.register(normalRequest);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser);
        assertEquals("arda@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertNotEquals("123456", savedUser.getPassword());


        verify(passwordEncoder).encode("12345");
    }

    @Test
    public void login_shouldAuthenticateAndSaveSecurityContextSuccessfully() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(normalLoginRequest.email(),
                                                                                normalLoginRequest.password());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);


        userService.login(normalLoginRequest, httpServletRequest, httpServletResponse);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void login_shouldNotSaveContextWhenAuthenticationFails() {
        LoginRequest badRequest = new LoginRequest("arda@mail.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
              .thenThrow(new BadCredentialsException("Bad credentials"));

      assertThrows(BadCredentialsException.class,
              () -> userService.login(badRequest,httpServletRequest,httpServletResponse));

      verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
      verify(securityContextRepository, never())
                .saveContext(any(SecurityContext.class), any(HttpServletRequest.class), any(HttpServletResponse.class));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}