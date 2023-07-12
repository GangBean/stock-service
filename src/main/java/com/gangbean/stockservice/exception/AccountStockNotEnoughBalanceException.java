package com.gangbean.stockservice.exception;

import com.gangbean.stockservice.exception.stock.StockServiceException;

public class AccountStockNotEnoughBalanceException extends StockServiceException {
    public AccountStockNotEnoughBalanceException(String message) {
        super(message);
    }
}
