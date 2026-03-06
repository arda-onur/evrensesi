package com.arda.evrensesi.mapper;

import com.arda.evrensesi.dto.UserDTO;
import com.arda.evrensesi.entity.User;
import com.arda.evrensesi.request.RegisterRequest;

public class UserMapper {
    private UserMapper() {}

    public static User toEntity(RegisterRequest req) {
        if (req == null) return null;

        User user = new User();
        user.setEmail(req.email());

        return user;
    }

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        return new UserDTO(
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
