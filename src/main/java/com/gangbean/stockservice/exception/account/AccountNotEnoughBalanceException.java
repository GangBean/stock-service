package com.gangbean.stockservice.exception.account;

public class AccountNotEnoughBalanceException extends AccountServiceException {
    public AccountNotEnoughBalanceException(String message) {
        super(message);
    }
}
