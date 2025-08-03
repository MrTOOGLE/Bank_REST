package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private TransferService transferService;

    @Test
    void shouldTransferSuccessfully() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Card fromCard = createActiveCard(owner, "1111222233334444", new BigDecimal("1000.00"));
        Card toCard = createActiveCard(owner, "5555666677778888", new BigDecimal("500.00"));

        BigDecimal transferAmount = new BigDecimal("300.00");

        when(cardService.findCardByNumber("1111222233334444")).thenReturn(fromCard);
        when(cardService.findCardByNumber("5555666677778888")).thenReturn(toCard);

        // Act
        transferService.transfer(owner, "1111222233334444", "5555666677778888", transferAmount);

        // Assert
        assertEquals(new BigDecimal("700.00"), fromCard.getBalance());
        assertEquals(new BigDecimal("800.00"), toCard.getBalance());

        verify(cardService).updateCardBalance(owner, fromCard, new BigDecimal("700.00"));
        verify(cardService).updateCardBalance(owner, toCard, new BigDecimal("800.00"));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Card fromCard = createActiveCard(owner, "1111222233334444", new BigDecimal("100.00"));
        Card toCard = createActiveCard(owner, "5555666677778888", new BigDecimal("500.00"));

        BigDecimal transferAmount = new BigDecimal("200.00"); // Больше чем на карте

        when(cardService.findCardByNumber("1111222233334444")).thenReturn(fromCard);
        when(cardService.findCardByNumber("5555666677778888")).thenReturn(toCard);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> transferService.transfer(owner, "1111222233334444", "5555666677778888", transferAmount));

        assertEquals("LOW_BALANCE", exception.getErrorCode());
        assertEquals("На вашей карте не хватает денег", exception.getMessage());

        // Баланс не должен измениться
        assertEquals(new BigDecimal("100.00"), fromCard.getBalance());
        assertEquals(new BigDecimal("500.00"), toCard.getBalance());
    }

    private Card createActiveCard(User owner, String number, BigDecimal balance) {
        Card card = new Card();
        card.setOwner(owner);
        card.setNumber(number);
        card.setBalance(balance);
        card.setStatus(Status.ACTIVE);
        card.setValidityPeriod(LocalDate.now().plusYears(1));
        return card;
    }
}