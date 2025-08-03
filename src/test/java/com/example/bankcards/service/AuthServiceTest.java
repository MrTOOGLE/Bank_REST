package com.example.bankcards.service;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ServiceException;
import com.example.bankcards.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String token = "jwt_token";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encoded_password");
        user.setRole(Role.USER);

        when(userService.findUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, "encoded_password")).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn(token);

        // Act
        String result = authService.login(email, password);

        // Assert
        assertEquals(token, result);
        verify(userService).findUserByEmail(email);
        verify(passwordEncoder).matches(password, "encoded_password");
        verify(jwtUtil).generateToken(user);
    }

    @Test
    void shouldThrowExceptionWhenWrongPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "wrong_password";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encoded_password");

        when(userService.findUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, "encoded_password")).thenReturn(false);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> authService.login(email, password));

        assertEquals("WRONG_PASSWORD", exception.getErrorCode());
        assertEquals("Неверный пароль", exception.getMessage());
    }
}