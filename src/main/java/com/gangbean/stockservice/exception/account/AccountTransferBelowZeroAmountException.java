package com.gangbean.stockservice.exception.account;

public class AccountTransferBelowZeroAmountException extends AccountServiceException {
    public AccountTransferBelowZeroAmountException(String message) {
        super(message);
    }
}
