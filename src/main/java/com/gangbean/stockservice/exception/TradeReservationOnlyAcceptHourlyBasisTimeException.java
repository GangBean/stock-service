package com.gangbean.stockservice.exception;

public class TradeReservationOnlyAcceptHourlyBasisTimeException extends RuntimeException {
    public TradeReservationOnlyAcceptHourlyBasisTimeException(String message) {
        super(message);
    }
}
