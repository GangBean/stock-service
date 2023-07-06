package com.gangbean.stockservice.domain;

public enum TradeType {
    DEPOSIT("입금"),
    WITHDRAW("출금"),
    PAYMENT("결제");

    private final String description;

    TradeType(String description) {
        this.description = description;
    }
}
