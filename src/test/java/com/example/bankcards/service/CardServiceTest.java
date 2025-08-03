package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ServiceException;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    void shouldCreateCardSuccessfully() {
        // Arrange
        User owner = new User();
        owner.setId(1L);
        owner.setName("Test User");

        when(cardRepository.findByNumber(anyString())).thenReturn(Optional.empty());

        // Act
        cardService.createCard(owner);

        // Assert
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void shouldBlockCardAsOwner() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Card card = new Card();
        card.setId(1L);
        card.setOwner(owner);
        card.setValidityPeriod(LocalDate.now().plusYears(1));
        card.setStatus(Status.ACTIVE);

        // Act
        cardService.blockCard(owner, card);

        // Assert
        assertEquals(Status.BLOCKED, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void shouldThrowExceptionWhenUserTriesToDeleteCard() {
        // Arrange
        User user = new User();
        user.setRole(Role.USER);

        Card card = new Card();
        card.setId(1L);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> cardService.deleteCard(user, card));

        assertEquals("ACCESS_DENIED", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("удаление карты"));

        verify(cardRepository, never()).delete(any());
    }
}