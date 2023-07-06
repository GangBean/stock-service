package com.gangbean.stockservice.domain;

public class Stock {
    private Long id;
    private String name;
    private Long price;
    private Long balance;

    public Stock(Long id, String name, Long price, Long balance) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.balance = balance;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Long howMuch() {
        return price;
    }

    public Long howMany() {
        return balance;
    }
}
