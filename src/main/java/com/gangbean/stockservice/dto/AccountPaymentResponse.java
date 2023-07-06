package com.gangbean.stockservice.dto;

public class AccountPaymentResponse {


    private final Long balance;

    public AccountPaymentResponse(Long balance) {
        this.balance = balance;
    }

    public static AccountPaymentResponse responseOf(Long balance) {
        return new AccountPaymentResponse(balance);
    }

    public Long balance() {
        return balance;
    }
}
