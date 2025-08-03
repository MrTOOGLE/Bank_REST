package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ErrorCode;
import com.example.bankcards.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransferService {
    private CardService cardService;

    @Transactional
    public void transfer(User owner, String fromCardNumber, String toCardNumber, BigDecimal amount) {
        Card fromCard = cardService.findCardByNumber(fromCardNumber);
        Card toCard = cardService.findCardByNumber(toCardNumber);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(ErrorCode.INVALID_AMOUNT, "Перевод должен быть больше нуля");
        }

        if (fromCardNumber.equals(toCardNumber)) {
            throw new ServiceException(ErrorCode.SAME_CARD, "Нельзя переводить на ту же карту");
        }

        if (!fromCard.getOwner().getId().equals(owner.getId()) || !toCard.getOwner().getId().equals(owner.getId())) {
            throw new ServiceException(ErrorCode.WRONG_CARD_OWNER, "Одна из ващих карт не принадлежит вам");
        }

        if (!fromCard.getStatus().equals(Status.ACTIVE) || !toCard.getStatus().equals(Status.ACTIVE)) {
            throw new ServiceException(ErrorCode.WRONG_CARD_STATUS, "Одна из ваших карт не активна");
        }

        if (fromCard.getBalance().compareTo(amount) >= 0) {
            fromCard.setBalance(fromCard.getBalance().subtract(amount));
            toCard.setBalance(toCard.getBalance().add(amount));
            cardService.updateCardBalance(owner, fromCard, fromCard.getBalance());
            cardService.updateCardBalance(owner, toCard, toCard.getBalance());
        } else {
            throw new ServiceException(ErrorCode.LOW_BALANCE, "На вашей карте не хватает денег");
        }
    }
}
