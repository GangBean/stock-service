package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.Stock;

public class StockInfoResponse {
    public static StockInfoResponse responseOf(Stock stock) {
        return new StockInfoResponse();
    }
}
