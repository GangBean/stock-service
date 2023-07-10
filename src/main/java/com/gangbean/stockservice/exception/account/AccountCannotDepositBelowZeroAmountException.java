package com.gangbean.stockservice.exception.account;

public class AccountCannotDepositBelowZeroAmountException extends AccountServiceException {

    public AccountCannotDepositBelowZeroAmountException(String message) {
        super(message);
    }
}
