package com.gangbean.stockservice.dto;

import lombok.Getter;

@Getter
public class StockSellRequest {
    private final Long stockId;

    private final Long accountId;

    private final Long amount;

    private final Long price;

    public StockSellRequest(Long stockId, Long accountId, Long amount, Long price) {
        this.stockId = stockId;
        this.accountId = accountId;
        this.amount = amount;
        this.price = price;
    }
}
