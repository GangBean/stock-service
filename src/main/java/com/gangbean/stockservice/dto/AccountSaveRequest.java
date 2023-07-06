package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.Account;

public class AccountSaveRequest {
    private final String bankName;
    private final Long bankNumber;
    private final Long balance;

    public AccountSaveRequest(String bankName, Long bankNumber, Long balance) {
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.balance = balance;
    }

    public static AccountSaveRequest requestOf(Account account) {
        return new AccountSaveRequest(account.bank().name(), account.bank().number(), account.balance());
    }

    public Account asAccount() {
        return new Account();
    }

    public String bankName() {
        return bankName;
    }

    public Long bankNumber() {
        return bankNumber;
    }

    public Long balance() {
        return balance;
    }
}
