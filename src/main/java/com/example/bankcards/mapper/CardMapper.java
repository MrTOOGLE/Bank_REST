package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.FullCardDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(source = "number", target = "cardNumber")
    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "owner.email", target = "ownerEmail")
    @Mapping(source = "owner.id", target = "ownerId")
    CardDto toCardDto(Card card);

    @Mapping(source = "number", target = "cardNumber")
    @Mapping(source = "owner.name", target = "ownerName")
    FullCardDto toFullCardDto(Card card);

    List<CardDto> toCardDtoList(List<Card> cards);
    List<FullCardDto> toFullCardDtoList(List<Card> cards);
}
