package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.AccountStockTrade;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StockBuyResponse {
    private Long stockId;
    private Long amount;
    private Long averagePrice;

    public StockBuyResponse(Long stockId, Long amount, Long averagePrice) {
        this.stockId = stockId;
        this.amount = amount;
        this.averagePrice = averagePrice;
    }

    public static StockBuyResponse responseOf(AccountStockTrade accountStockTrade, List<AccountStockTrade> buyList) {
        Long totalSum = buyList.stream()
                .mapToLong(AccountStockTrade::totalAmount)
                .sum();
        Long totalCount = buyList.stream()
                .mapToLong(AccountStockTrade::totalCount)
                .sum();
        return new StockBuyResponse(accountStockTrade.stock().id()
                , accountStockTrade.balance()
                , totalSum / totalCount);
    }
}
