package com.gangbean.stockservice.exception.account;

public class TradeBetweenSameAccountsException extends AccountServiceException {
    public TradeBetweenSameAccountsException(String message) {
        super(message);
    }
}
