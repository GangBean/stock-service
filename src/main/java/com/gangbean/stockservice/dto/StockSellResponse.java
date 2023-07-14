package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.AccountStock;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class StockSellResponse {
    private Long stockId;
    private BigDecimal amount;
    private BigDecimal averagePrice;

    public StockSellResponse(Long stockId, BigDecimal amount, BigDecimal averagePrice) {
        this.stockId = stockId;
        this.amount = amount;
        this.averagePrice = averagePrice;
    }

    public static StockSellResponse responseOf(AccountStock accountStock) {
        return new StockSellResponse(accountStock.what().id(), accountStock.howMany(), accountStock.howMuch());
    }
}