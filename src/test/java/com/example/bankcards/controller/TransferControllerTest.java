package com.example.bankcards.controller;

import com.example.bankcards.dto.Request.TransferRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private TransferService transferService;

    private TransferController transferController;

    @BeforeEach
    void setUp() {
        transferController = new TransferController(transferService);
    }

    @Test
    void shouldTransferSuccessfully() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromCardNumber("1111222233334444");
        transferRequest.setToCardNumber("5555666677778888");
        transferRequest.setAmount(new BigDecimal("100.50"));

        // Act
        transferController.transfers(userDetails, transferRequest);

        // Assert
        verify(transferService).transfer(
                user,
                "1111222233334444",
                "5555666677778888",
                new BigDecimal("100.50")
        );
    }
}