package com.gangbean.stockservice.exception;

import com.gangbean.stockservice.exception.stock.StockServiceException;

public class AccountStockBuyBelowZeroException extends StockServiceException {
    public AccountStockBuyBelowZeroException(String message) {
        super(message);
    }
}
