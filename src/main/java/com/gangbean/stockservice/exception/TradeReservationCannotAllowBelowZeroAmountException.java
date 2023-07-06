package com.gangbean.stockservice.exception;

public class TradeReservationCannotAllowBelowZeroAmountException extends RuntimeException {
    public TradeReservationCannotAllowBelowZeroAmountException(String message) {
        super(message);
    }
}
