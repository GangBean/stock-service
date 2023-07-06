package com.gangbean.stockservice.dto;

public class AccountTransferResponse {

    private final Long balance;

    public AccountTransferResponse(Long balance) {
        this.balance = balance;
    }

    public static AccountTransferResponse responseOf(Long balance) {
        return new AccountTransferResponse(balance);
    }

    public Long balance() {
        return balance;
    }
}
