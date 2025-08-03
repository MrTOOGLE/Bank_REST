package com.example.bankcards.controller;

import com.example.bankcards.dto.Request.LoginRequest;
import com.example.bankcards.dto.Request.RegisterRequest;
import com.example.bankcards.dto.Response.AuthResponse;
import com.example.bankcards.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String token = "jwt_token";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        when(authService.login(email, password)).thenReturn(token);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().getToken());
        verify(authService).login(email, password);
    }

    @Test
    void shouldRegisterSuccessfully() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setName("New User");

        // Act
        authController.register(registerRequest);

        // Assert
        verify(authService).register("newuser@example.com", "password123", "New User");
    }
}