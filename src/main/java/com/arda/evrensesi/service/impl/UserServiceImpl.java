package com.arda.evrensesi.service.impl;

import com.arda.evrensesi.config.PasswordEncryption;
import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.entity.User;
import com.arda.evrensesi.exception.UserRegistrationException;
import com.arda.evrensesi.mapper.UserMapper;
import com.arda.evrensesi.repository.UserRepository;
import com.arda.evrensesi.request.UserRequest;
import com.arda.evrensesi.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncryption passwordEncryption;

    public UserServiceImpl(UserRepository userRepository, PasswordEncryption passwordEncryption) {
        this.userRepository = userRepository;
        this.passwordEncryption = passwordEncryption;
    }

    @Transactional
    public UserDTO register(@NotNull UserRequest userRequest){
        checkRegistrationEligibility(userRequest);

        User user = UserMapper.toEntity(userRequest);
        user.setPassword(passwordEncryption.passwordEncoder().encode(userRequest.password()));

        try {
            this.userRepository.save(user);
            return UserMapper.toDTO(user);
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            throw new UserRegistrationException("user.already.exists", userRequest.email());
        }

    }

    private void checkRegistrationEligibility(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.email()))
            throw new UserRegistrationException("user.already.exists", userRequest.email());

        if (!userRequest.password().equals(userRequest.rePassword()))
            throw new UserRegistrationException("user.request.validation.password.mismatch");
    }

}
