package com.gangbean.stockservice.domain;

public class AccountStock {

    private Account account;
    private Stock stock;
    private Long balance;
    private Long averagePrice;

    public AccountStock(Account account, Stock stock, Long balance, Long averagePrice) {
        this.account = account;
        this.stock = stock;
        this.balance = balance;
        this.averagePrice = averagePrice;
    }

    public Account account() {
        return account;
    }

    public Stock stock() {
        return stock;
    }

    public Long balance() {
        return balance;
    }

    public Long averagePrice() {
        return averagePrice;
    }
}
