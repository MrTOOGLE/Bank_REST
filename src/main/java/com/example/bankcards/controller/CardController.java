package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.Request.CreateCardRequest;
import com.example.bankcards.dto.Request.SetCardStatusRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
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
@RequestMapping("/api/v1/cards")
public class CardController {
    private final UserService userService;
    private CardService cardService;
    private final CardMapper cardMapper;

    @PostMapping
    public ResponseEntity<String> createCard(@Valid @RequestBody CreateCardRequest createCardRequest) {
        User user = userService.findUserById(createCardRequest.getUserId());
        cardService.createCard(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<CardDto>> getCards(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        User user = customUserDetails.getUser();
        Page<Card> cards = cardService.getAllCards(user, pageable);
        Page<CardDto> cardDtos = cards.map(cardMapper::toCardDto);
        return ResponseEntity.ok(cardDtos);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        return ResponseEntity.ok(cardService.getBalance(user, card));
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                          @PathVariable Long id, @Valid @RequestBody SetCardStatusRequest setCardStatusRequest) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        cardService.updateCardStatus(user, card, setCardStatusRequest.getStatus());
    }

    @PutMapping("/{id}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setBlock(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        cardService.blockCard(user, card);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id) {
        User user = customUserDetails.getUser();
        Card card = cardService.findCardById(id);
        cardService.deleteCard(user, card);
    }
}
