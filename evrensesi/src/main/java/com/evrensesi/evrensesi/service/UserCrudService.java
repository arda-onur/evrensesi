package com.evrensesi.evrensesi.service;

import com.evrensesi.evrensesi.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserCrudService {
    public User createUser(User user);
}
