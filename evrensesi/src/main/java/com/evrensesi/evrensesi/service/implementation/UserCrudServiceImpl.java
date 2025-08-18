package com.evrensesi.evrensesi.service.implementation;

import com.evrensesi.evrensesi.constant.Role;
import com.evrensesi.evrensesi.exception.UserAlreadyExistsException;
import com.evrensesi.evrensesi.model.User;
import com.evrensesi.evrensesi.repository.UserRepository;
import com.evrensesi.evrensesi.request.UserRequest;
import com.evrensesi.evrensesi.service.JwtService;
import com.evrensesi.evrensesi.service.UserCrudService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCrudServiceImpl implements UserCrudService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(User user) {
        Objects.requireNonNull(user, "Argument 'user' can not be null!");
        this.userRepository.findUserByUsernameOrEmail(user.getUsername(), user.getEmail())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException("Username or Email already exist!", user.getUsername());
                });
     user.setPassword(passwordEncoder.encode(user.getPassword()));
     user.setRole(Role.USER);
     this.userRepository.save(user);

     return user;
    }

    @Override
    public User authenticateUser(UserRequest userRequest,HttpServletResponse response) {
        Objects.requireNonNull(userRequest, "Argument 'User' can not be null!");

       User user = this.userRepository.findUserByUsername(userRequest.username())
               .orElseThrow(() -> new UsernameNotFoundException("User not found"));

       if (!passwordEncoder.matches(userRequest.password(), user.getPassword())) {
           throw new BadCredentialsException("Invalid password!");
       }

     this.authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password())
     );

        response.setHeader("Authorization", "Bearer " + jwtService.generateToken(user));
       return user;
    }


}
