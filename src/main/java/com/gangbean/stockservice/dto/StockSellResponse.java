package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.AccountStockTrade;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StockSellResponse {
    private Long stockId;
    private Long amount;
    private Long averagePrice;

    public StockSellResponse(Long stockId, Long amount, Long averagePrice) {
        this.stockId = stockId;
        this.amount = amount;
        this.averagePrice = averagePrice;
    }

    public static StockSellResponse responseOf(AccountStockTrade accountStockTrade, List<AccountStockTrade> buyList) {
        return new StockSellResponse();
    }
}
