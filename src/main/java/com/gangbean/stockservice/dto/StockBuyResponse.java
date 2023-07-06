package com.gangbean.stockservice.dto;

import lombok.Getter;

@Getter
public class StockBuyResponse {
    private final Long stockId;
    private final Long amount;
    private final Long averagePrice;

    public StockBuyResponse(Long stockId, Long amount, Long averagePrice) {
        this.stockId = stockId;
        this.amount = amount;
        this.averagePrice = averagePrice;
    }

    public static StockBuyResponse responseOf() {
        return null;
    }
}
