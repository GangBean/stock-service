package com.gangbean.stockservice.exception;

import com.gangbean.stockservice.exception.stock.StockServiceException;

public class AccountStockBuyAmountBelowZeroException extends StockServiceException {
    public AccountStockBuyAmountBelowZeroException(String message) {
        super(message);
    }
}
