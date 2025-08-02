package com.example.bankcards.controller;

import com.example.bankcards.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cards")
public class CardController {
    private CardService cardService;

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        
    }
}
