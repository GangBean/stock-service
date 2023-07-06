package com.gangbean.stockservice.dto;

public class AccountNotEnoughBalanceException extends RuntimeException {
    public AccountNotEnoughBalanceException(String message) {
        super(message);
    }
}
