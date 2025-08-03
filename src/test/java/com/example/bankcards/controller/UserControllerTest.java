package com.example.bankcards.controller;

import com.example.bankcards.dto.Request.CreateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, userMapper);
    }

    @Test
    void shouldGetUsersSuccessfully() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setRole(Role.ADMIN);

        UserDto userDto1 = new UserDto();
        userDto1.setId("1");
        userDto1.setEmail("user1@example.com");

        UserDto userDto2 = new UserDto();
        userDto2.setId("2");
        userDto2.setEmail("user2@example.com");

        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toUserDto(user1)).thenReturn(userDto1);
        when(userMapper.toUserDto(user2)).thenReturn(userDto2);

        // Act
        ResponseEntity<List<UserDto>> response = userController.getUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService).findAll();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("newuser@example.com");
        createUserRequest.setName("New User");
        createUserRequest.setPassword("password123");
        createUserRequest.setRole(Role.USER);

        // Act
        userController.createUser(createUserRequest);

        // Assert
        verify(userService).createUser(any(User.class));
    }
}