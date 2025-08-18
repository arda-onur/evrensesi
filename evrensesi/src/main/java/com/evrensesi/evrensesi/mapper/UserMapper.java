package com.evrensesi.evrensesi.mapper;

import com.evrensesi.evrensesi.dto.UserDto;
import com.evrensesi.evrensesi.model.User;
import com.evrensesi.evrensesi.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfiguration.class)
@Component
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    User map(UserRequest userRequest);
    UserDto map(User user);
}
