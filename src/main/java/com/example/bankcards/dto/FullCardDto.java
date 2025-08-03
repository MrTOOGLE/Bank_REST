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
public class FullCardDto {
    private String cardNumber;
    private LocalDate validityPeriod;
    private Status status;
    private String ownerName;
    private BigDecimal balance;
}
