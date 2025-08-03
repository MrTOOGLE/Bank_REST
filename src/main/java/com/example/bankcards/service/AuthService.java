package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ServiceException;
import com.example.bankcards.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(String email, String password) {
        User user = userService.getUserByEmail(email).orElseThrow(() -> new ServiceException("USER_NOT_EXISTS", "Такого пользователя не существует"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("WRONG_PASSWORD", "Неверный пароль");
        }
        return jwtUtil.generateToken(user);
    }

    public User register(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        return userService.createUser(user);
    }

    public User getCurrentUser(String token) {
        if (!jwtUtil.isTokenValid(token)) {
            throw new ServiceException("INVALID_TOKEN", "Токен недействителен");
        }

        String email = jwtUtil.extractEmail(token);
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new ServiceException("USER_NOT_FOUND", "Пользователь не найден"));
    }
}
