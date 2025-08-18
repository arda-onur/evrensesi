package com.evrensesi.evrensesi.service;

import com.evrensesi.evrensesi.dto.UserDto;
import com.evrensesi.evrensesi.model.User;
import com.evrensesi.evrensesi.request.UserRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface UserCrudService {
    User createUser(User user);
    User authenticateUser(UserRequest userRequest,HttpServletResponse response);
}
