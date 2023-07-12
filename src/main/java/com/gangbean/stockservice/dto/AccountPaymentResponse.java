package com.gangbean.stockservice.dto;

import java.math.BigDecimal;

public class AccountPaymentResponse {


    private final BigDecimal balance;

    public AccountPaymentResponse(BigDecimal balance) {
        this.balance = balance;
    }

    public static AccountPaymentResponse responseOf(BigDecimal balance) {
        return new AccountPaymentResponse(balance);
    }

    public BigDecimal balance() {
        return balance;
    }
}
