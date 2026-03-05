package com.arda.evrensesi.service.impl;

import com.arda.evrensesi.config.PasswordEncryption;
import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.entity.User;
import com.arda.evrensesi.exception.UserRegistrationException;
import com.arda.evrensesi.mapper.UserMapper;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.UserRequest;
import com.arda.evrensesi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncryption passwordEncryption;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncryption passwordEncryption,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncryption = passwordEncryption;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserDTO register(@NotNull UserRequest userRequest){
        checkRegistrationEligibility(userRequest);

        User user = UserMapper.toEntity(userRequest);
        user.setPassword(passwordEncryption.passwordEncoder().encode(userRequest.password()));

        try {
            this.userRepository.save(user);
            log.info("User registered = {}", userRequest.email());
            return UserMapper.toDTO(user);
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            throw new UserRegistrationException("user.already.exists", userRequest.email());
        }

    }

  public void login(UserRequest userRequest, HttpServletRequest httpRequest){
      Authentication authentication = authenticate(userRequest);
      SecurityContext context = createSecurityContext(authentication);
      storeSecurityContextInSession(httpRequest, context);
      log.info("User logged in = {}", userRequest.email());
  }


    private void checkRegistrationEligibility(UserRequest userRequest) {
       log.info("Checking registration eligibility = {}", userRequest.email());
        if (userRepository.existsByEmail(userRequest.email()))
            throw new UserRegistrationException("user.already.exists", userRequest.email());

        if (!userRequest.password().equals(userRequest.rePassword()))
            throw new UserRegistrationException("user.request.validation.password.mismatch");
    }
    private Authentication authenticate(UserRequest userRequest) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(userRequest.email(), userRequest.password());

        return authenticationManager.authenticate(authRequest);
    }

    private SecurityContext createSecurityContext(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        return context;
    }

    private void storeSecurityContextInSession(HttpServletRequest request, SecurityContext context) {
        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );
    }

    @NotNull
    @Override
    public UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
