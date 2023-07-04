package com.gangbean.stockservice.exception;

public class AccountNotExistsException extends RuntimeException {
    public AccountNotExistsException(String message) {
        super(message);
    }
}
