package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.AccountStockTrade;
import lombok.Getter;

import java.util.List;

@Getter
public class StockSellResponse {
    private final Long stockId;
    private final Long amount;
    private final Long averagePrice;

    public StockSellResponse(Long stockId, Long amount, Long averagePrice) {
        this.stockId = stockId;
        this.amount = amount;
        this.averagePrice = averagePrice;
    }

    public static StockSellResponse responseOf(AccountStockTrade accountStockTrade, List<AccountStockTrade> buyList) {
        Long totalSum = buyList.stream()
                .mapToLong(AccountStockTrade::totalAmount)
                .sum();
        Long totalCount = buyList.stream()
                .mapToLong(AccountStockTrade::totalCount)
                .sum();
        return new StockSellResponse(accountStockTrade.stock().id()
                , accountStockTrade.balance()
                , totalSum / totalCount);
    }
}
