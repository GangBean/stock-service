package com.gangbean.stockservice.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class AccountStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Stock stock;

    private Long balance;

    private Long price;

    public AccountStock() {
    }

    public AccountStock(Account account, Stock stock, Long balance, Long price) {
        this.account = account;
        this.stock = stock;
        this.balance = balance;
        this.price = price;
    }

    public AccountStock(Long id, Account account, Stock stock, Long balance, Long price) {
        this(account, stock, balance, price);
        this.id = id;
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

    public Long price() {
        return price;
    }

    public Long id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountStock that = (AccountStock) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
