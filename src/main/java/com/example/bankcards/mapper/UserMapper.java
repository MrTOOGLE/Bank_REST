package com.example.bankcards.mapper;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface UserMapper {
    @Mapping(source = "role", target = "role")
    UserDto toUserDto(User user);
}
