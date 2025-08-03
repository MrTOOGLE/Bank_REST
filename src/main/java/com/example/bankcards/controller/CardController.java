package com.example.bankcards.controller;

import com.example.bankcards.entity.User;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cards")
public class CardController {
    private CardService cardService;

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        
    }

    @GetMapping("/test")
    public ResponseEntity<String> testAuth(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok("Привет, " + user.getName() + "! Роль: " + user.getRole());
    }

    // Тест админских прав
    @PostMapping("/admin-test")
    public ResponseEntity<String> testAdmin(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok("Доступ админа работает! Пользователь: " + userDetails.getUser().getName());
    }
}
