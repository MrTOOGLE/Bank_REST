package com.example.bankcards.exception;

public enum ErrorCode {
    USER_NOT_EXISTS("USER_NOT_EXISTS"),
    USER_NOT_FOUND("USER_NOT_FOUND"),
    USER_EXISTS("USER_EXISTS"),
    WRONG_PASSWORD("WRONG_PASSWORD"),

    CARD_NOT_FOUND("CARD_NOT_FOUND"),
    CARD_EXPIRED("CARD_EXPIRED"),
    NOT_OWNER("NOT_OWNER"),

    INVALID_AMOUNT("INVALID_AMOUNT"),
    SAME_CARD("SAME_CARD"),
    WRONG_CARD_OWNER("WRONG_CARD_OWNER"),
    WRONG_CARD_STATUS("WRONG_CARD_STATUS"),
    LOW_BALANCE("LOW_BALANCE"),

    ACCESS_DENIED("ACCESS_DENIED");

    private final String code;
    ErrorCode(String code) {
        this.code = code;
    }
}
