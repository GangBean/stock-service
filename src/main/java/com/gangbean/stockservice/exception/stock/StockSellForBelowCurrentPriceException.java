package com.gangbean.stockservice.exception.stock;

public class StockSellForBelowCurrentPriceException extends StockServiceException {
    public StockSellForBelowCurrentPriceException(String message) {
        super(message);
    }
}
