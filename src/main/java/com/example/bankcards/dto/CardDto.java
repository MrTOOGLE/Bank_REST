package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private String cardNumber;
    private LocalDate validityPeriod;
    private Status status;
    private BigDecimal balance;

    public String getCardNumber() {
        if (cardNumber != null && cardNumber.length() == 16) {
            return "**** **** **** " + cardNumber.substring(12);
        }
        return cardNumber;
    }
}
