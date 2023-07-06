package com.gangbean.stockservice.exception;

public class StockAmountNotValidException extends RuntimeException {
    public StockAmountNotValidException(String message) {
        super(message);
    }
}
