package com.zemoso.figma.growthcapital.mapper;

import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.User;

public class UserMapper {
    // Convert UserDto into User JPA Entity
    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    private UserMapper() {

    }
}
