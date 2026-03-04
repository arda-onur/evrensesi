package com.arda.evrensesi.service;

import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.request.UserRequest;


public interface UserService {
    UserDTO register(UserRequest userRequest);
}
