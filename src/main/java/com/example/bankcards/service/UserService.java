package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ErrorCode;
import com.example.bankcards.exception.ServiceException;
import com.example.bankcards.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setPassword(encryptPassword(user.getPassword()));
            userRepository.save(user);
        } else {
            throw new ServiceException(ErrorCode.USER_EXISTS, "Пользователь с почтой " + user.getEmail() + " уже существует");
        }
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void updateUserProfile(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            throw new ServiceException(ErrorCode.USER_NOT_EXISTS, "Такого пользователя не существует");
        }
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND, "Пользователь не найден"));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND, "Пользователь не найден"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
