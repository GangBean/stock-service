package com.gangbean.stockservice.exception.account;

public class BankNotFoundException extends AccountServiceException {

    public BankNotFoundException(String message) {
        super(message);
    }
}
