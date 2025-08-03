package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ServiceException;
import com.example.bankcards.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setPassword(encryptPassword(user.getPassword()));
            return userRepository.save(user);
        }
        throw new ServiceException("USER_EXISTS", "Пользователь с почтой " + user.getEmail() + " уже существует");
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User updateUserProfile(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            throw new ServiceException("USER_NOT_EXISTS", "Такого пользователя не существует");
        }
        return userRepository.save(user);
    }

    public User updateUserPassword(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            User user = userRepository.findByEmail(email).get();
            user.setPassword(passwordEncoder.encode(password));
            return userRepository.save(user);
        }
        throw new ServiceException("USER_NOT_EXISTS", "Такого пользователя не существует");
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ServiceException("USER_NOT_FOUND", "Пользователь не найден"));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ServiceException("USER_NOT_FOUND", "Пользователь не найден"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
