package com.gangbean.stockservice.exception;

public class StockNotEnoughBalanceException extends RuntimeException {
    public StockNotEnoughBalanceException(String message) {
        super(message);
    }
}
