package com.gangbean.stockservice.exception.account;

public class AccountNotExistsException extends AccountServiceException {
    public AccountNotExistsException(String message) {
        super(message);
    }
}
