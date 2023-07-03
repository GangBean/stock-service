package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.entity.Account;

public class AccountInfoResponse {

    private Long id;
    private String accountNumber;
    private String bankName;
    private Long bankNumber;
    private Long balance;

    public AccountInfoResponse(Long id, String accountNumber, String bankName, Long bankNumber, Long balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.balance = balance;
    }

    public Long id() {
        return id;
    }

    public String accountNumber() {
        return accountNumber;
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

    public static AccountInfoResponse responseOf(Account account) {
        return new AccountInfoResponse(account.id()
                , account.number()
                , account.bank().name()
                , account.bank().number()
                , account.balance());
    }
}
