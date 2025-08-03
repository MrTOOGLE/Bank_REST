package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<User> users = userService.findAll();
        List<UserDto> userDtos = users.stream().map(user -> userMapper.toUserDto(user)).toList();
        return ResponseEntity.ok(userDtos);
    }

    @DeleteMapping("/id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam("id") Long id) {
        User user = userService.findUserById(id);
        userService.deleteUser(user);
    }
}
