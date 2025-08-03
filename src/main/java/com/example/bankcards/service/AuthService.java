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
        User user = userService.findUserByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("WRONG_PASSWORD", "Неверный пароль");
        }
        return jwtUtil.generateToken(user);
    }

    public void register(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        userService.createUser(user);
    }
}
