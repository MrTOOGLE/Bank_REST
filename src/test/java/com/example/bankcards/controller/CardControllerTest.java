package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.Request.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private UserService userService;

    @Mock
    private CardMapper cardMapper;

    private CardController cardController;

    @BeforeEach
    void setUp() {
        cardController = new CardController(userService, cardService, cardMapper);
    }

    @Test
    void shouldCreateCardSuccessfully() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        CreateCardRequest createCardRequest = new CreateCardRequest();
        createCardRequest.setUserId(1L);

        when(userService.findUserById(1L)).thenReturn(user);

        // Act
        ResponseEntity<String> response = cardController.createCard(createCardRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).findUserById(1L);
        verify(cardService).createCard(user);
    }

    @Test
    void shouldGetCardsSuccessfully() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Card card = new Card();
        card.setId(1L);
        card.setNumber("1234567890123456");
        card.setStatus(Status.ACTIVE);
        card.setValidityPeriod(LocalDate.now().plusYears(1));
        card.setBalance(BigDecimal.valueOf(1000));

        CardDto cardDto = new CardDto();
        cardDto.setCardId("1");
        cardDto.setCardNumber("**** **** **** 3456");

        Page<Card> cardsPage = new PageImpl<>(Arrays.asList(card), PageRequest.of(0, 10), 1);
        Page<CardDto> cardDtosPage = new PageImpl<>(Arrays.asList(cardDto), PageRequest.of(0, 10), 1);

        Pageable pageable = PageRequest.of(0, 10);

        when(cardService.getAllCards(user, pageable)).thenReturn(cardsPage);
        when(cardMapper.toCardDto(card)).thenReturn(cardDto);

        // Act
        ResponseEntity<Page<CardDto>> response = cardController.getCards(userDetails, pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(cardService).getAllCards(user, pageable);
    }

    @Test
    void shouldBlockCardSuccessfully() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Card card = new Card();
        card.setId(1L);
        card.setOwner(user);
        card.setStatus(Status.ACTIVE);
        card.setValidityPeriod(LocalDate.now().plusYears(1));

        when(cardService.findCardById(1L)).thenReturn(card);

        // Act
        cardController.setBlock(userDetails, 1L);

        // Assert
        verify(cardService).findCardById(1L);
        verify(cardService).blockCard(user, card);
    }
}