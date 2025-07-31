package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ServiceException;
import com.example.bankcards.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardService {
    CardRepository cardRepository;

    public Card createCard(User owner) {
        Card card = new Card();
        card.setNumber(generateCardNumber());
        card.setOwner(owner);
        card.setValidityPeriod(LocalDate.now().plusYears(3));

        return cardRepository.save(card);
    }

    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }

    public Card updateCardBalance(User user, Card card, BigDecimal balance) {
        checkValidityPeriod(card);

        if (checkOwner(user, card) || user.getRole().equals(Role.ADMIN)) {
            card.setBalance(balance);
            return cardRepository.save(card);
        }
        throw new ServiceException("NOT_OWNER", "Вы не являетесь собственником этой карты");
    }

    public Card updateCardStatus(User user, Card card, Status status) {
        checkValidityPeriod(card);

        if (user.getRole().equals(Role.ADMIN)) {
            card.setStatus(status);
            return cardRepository.save(card);
        }
        throw new ServiceException("ACCESS_DENIED", "У вас нет прав на изменение статуса карты");
    }

    public Page<Card> getAllCards(User user, Pageable pageable) {
        Page<Card> cards;

        if (user.getRole() == Role.ADMIN) {
            cards = cardRepository.findAll(pageable);
        } else {
            cards = cardRepository.findByOwnerId(user.getId(), pageable);
        }

        List<Card> checkCardsValidityPeriod = cards.getContent().stream()
                .filter(card -> card.getValidityPeriod().isBefore(LocalDate.now()) && card.getStatus() != Status.EXPIRED)
                .peek(card -> card.setStatus(Status.EXPIRED))
                .toList();
        cardRepository.saveAll(checkCardsValidityPeriod);

        return cards;
    }

    public BigDecimal getBalance(User user, Card card) {
        checkValidityPeriod(card);

        if (checkOwner(user, card)) {
            return card.getBalance();
        }
        throw new ServiceException("NOT_OWNER", "Вы не являетесь собственником этой карты");
    }

    public Card findCardByNumber(String number) {
        return cardRepository.findByNumber(number).orElseThrow(() -> new ServiceException("CARD_NOT_FOUND", "Карта не найдена"));
    }

    public Card blockCard(User user, Card card) {
        checkValidityPeriod(card);

        if (card.getStatus() == Status.EXPIRED) {
            throw new ServiceException("CARD_EXPIRED", "Нельзя заблокировать просроченную карту");
        }

        if (checkOwner(user, card)) {
            card.setStatus(Status.BLOCKED);
            return cardRepository.save(card);
        }
        throw new ServiceException("NOT_OWNER", "Вы не являетесь собственником этой карты");
    }

    private boolean checkOwner(User user, Card card) {
        return card.getOwner().getId().equals(user.getId());
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }

        if (cardRepository.findByNumber(cardNumber.toString()).isPresent()) {
            cardNumber = new StringBuilder(generateCardNumber());
        }

        return cardNumber.toString();
    }

    private void checkValidityPeriod(Card card) {
        if (card.getValidityPeriod().isBefore(LocalDate.now()) && card.getStatus() != Status.EXPIRED) {
            card.setStatus(Status.EXPIRED);
            cardRepository.save(card);
        }
    }
}
