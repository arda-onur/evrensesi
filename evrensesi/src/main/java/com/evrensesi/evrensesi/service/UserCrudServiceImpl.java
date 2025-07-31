package com.evrensesi.evrensesi.service;

import com.evrensesi.evrensesi.exception.UserAlreadyExistsException;
import com.evrensesi.evrensesi.model.User;
import com.evrensesi.evrensesi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@RequiredArgsConstructor
@Log4j2
public class UserCrudServiceImpl implements UserCrudService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User createUser(User user) {
        Objects.requireNonNull(user, "Argument 'user' can not be null!");
        log.info("Creating new user with username ({}).", user.getUsername());

        this.userRepository.findUserByUsername(user.getUsername())
                .ifPresent(u -> {
                    log.warn("Username already exists: {}", user.getUsername());
                    throw new UserAlreadyExistsException("user.already.exists.message", user.getUsername());

                });
     user.setPassword(passwordEncoder.encode(user.getPassword()));
     this.userRepository.save(user);
     return user;
    }
}
