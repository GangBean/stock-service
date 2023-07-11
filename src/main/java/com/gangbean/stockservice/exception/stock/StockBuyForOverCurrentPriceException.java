package com.gangbean.stockservice.exception.stock;

public class StockBuyForOverCurrentPriceException extends StockServiceException {
    public StockBuyForOverCurrentPriceException(String message) {
        super(message);
    }
}
