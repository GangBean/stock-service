package com.gangbean.stockservice.exception;

public class AccoutCannotDepositBelowZeroAmountException extends RuntimeException {

    public AccoutCannotDepositBelowZeroAmountException(String message) {
        super(message);
    }
}
