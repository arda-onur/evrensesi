package com.arda.evrensesi;

import com.arda.evrensesi.config.PasswordEncryption;
import com.arda.evrensesi.entity.User;
import com.arda.evrensesi.exception.UserRegistrationException;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.UserRequest;
import com.arda.evrensesi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncryption passwordEncryption;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

     @Test
    void register_shouldThrowException_whenEmailExists() {
         String email ="test@mail.com";
         UserRequest request = new UserRequest(email,"123","123");

         when(userRepository.existsByEmail(email)).thenReturn(true);
         assertThrows(UserRegistrationException.class, () -> this.userService.register(request));

         verify(userRepository).existsByEmail("test@mail.com");
         verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowException_whenPasswordsMismatch() {
        UserRequest request = new UserRequest("test@mail.com","123","456");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);

        assertThrows(UserRegistrationException.class,() -> userService.register(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_shouldSaveUser() {

        UserRequest request = new UserRequest("test@mail.com", "123", "123");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);

        when(passwordEncryption.passwordEncoder()).thenReturn(passwordEncoder);

        when(passwordEncoder.encode("123")).thenReturn("encoded");

        userService.register(request);

        verify(userRepository).save(any(User.class));
    }
}


