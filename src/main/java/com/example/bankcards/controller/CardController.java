package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cards")
public class CardController {
    private CardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCard(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody CreateCardRequest createCardRequest) {
        cardService.createCard(customUserDetails.getUser());
    }

    @GetMapping
    public ResponseEntity<Page<Card>> getCards(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        User user = customUserDetails.getUser();
        Page<Card> cards = cardService.getAllCards(user, pageable);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        return ResponseEntity.ok(cardService.getBalance(user, card));
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.CREATED)
    public void setStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @PathVariable Long id, @Valid @RequestBody Status status) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        cardService.updateCardStatus(user, card, status);
    }

    @PutMapping("/{id}/block")
    @ResponseStatus(HttpStatus.CREATED)
    public void setBlock(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        cardService.blockCard(user, card);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteCard(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        cardService.deleteCard(user, card);
    }
}
