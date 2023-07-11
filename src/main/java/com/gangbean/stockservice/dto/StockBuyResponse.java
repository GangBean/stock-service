package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.AccountStock;
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

    public static StockBuyResponse responseOf(AccountStock accountStock, List<AccountStock> buyList) {
        Long totalSum = buyList.stream()
                .mapToLong(AccountStock::totalAmount)
                .sum();
        Long totalCount = buyList.stream()
                .mapToLong(AccountStock::totalCount)
                .sum();
        return new StockBuyResponse(accountStock.stock().id()
                , accountStock.balance()
                , totalSum / totalCount);
    }
}
