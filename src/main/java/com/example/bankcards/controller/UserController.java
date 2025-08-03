package com.example.bankcards.controller;

import com.example.bankcards.dto.Request.CreateUserRequest;
import com.example.bankcards.dto.Request.UpdateUserRoleRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> users = userService.findAll();
        List<UserDto> userDtos = users.stream().map(user -> userMapper.toUserDto(user)).toList();
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setRole(createUserRequest.getRole());

        userService.createUser(user);
    }

    @PutMapping("/{id}/role")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserRole(@PathVariable("id") Long id,@Valid @RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        User user = userService.findUserById(id);
        user.setRole(updateUserRoleRequest.getRole());
        userService.updateUserProfile(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        userService.deleteUser(user);
    }
}
