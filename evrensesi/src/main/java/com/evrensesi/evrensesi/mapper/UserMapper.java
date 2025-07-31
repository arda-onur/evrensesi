package com.evrensesi.evrensesi.mapper;

import com.evrensesi.evrensesi.dto.UserDto;
import com.evrensesi.evrensesi.model.User;
import com.evrensesi.evrensesi.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    User map(UserRequest userRequest);

    UserDto map(User user);
}
