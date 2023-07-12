package com.gangbean.stockservice.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class StockSellRequest {
    private Long stockId;

    private Long accountId;

    private BigDecimal amount;

    private BigDecimal price;

    public StockSellRequest(Long stockId, Long accountId, BigDecimal amount, BigDecimal price) {
        this.stockId = stockId;
        this.accountId = accountId;
        this.amount = amount;
        this.price = price;
    }
}
