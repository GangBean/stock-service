package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.Stock;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StockDetailInfoResponse {
    private final Long stockId;
    private final String stockName;
    private final List<StockHistoryInfoResponse> histories;

    public StockDetailInfoResponse(Long stockId, String stockName, List<StockHistoryInfoResponse> histories) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.histories = histories;
    }

    public static StockDetailInfoResponse responseOf(Stock stock) {
        return new StockDetailInfoResponse(stock.id(), stock.name(),
                stock.histories().stream()
                        .map(StockHistoryInfoResponse::responseOf)
                        .collect(Collectors.toList()));
    }
}
