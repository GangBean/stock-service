package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.TradeReservation;

import java.time.LocalDateTime;

public class PaymentReservationResponse {

    private final Long accountId;
    private final Long balance;
    private final LocalDateTime sendAt;

    public PaymentReservationResponse(Long accountId, Long balance, LocalDateTime sendAt) {
        this.accountId = accountId;
        this.balance = balance;
        this.sendAt = sendAt;
    }

    public static PaymentReservationResponse responseOf(TradeReservation tradeReservation) {
        return new PaymentReservationResponse(tradeReservation.from().id()
                , tradeReservation.from().balance(), tradeReservation.when());
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getBalance() {
        return balance;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }
}
