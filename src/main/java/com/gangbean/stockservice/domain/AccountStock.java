package com.gangbean.stockservice.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class AccountStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Account account;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Stock stock;

    @Enumerated(EnumType.STRING)
    private StockTradeType tradeType;

    private Long balance;

    private Long price;

    public AccountStock() {
    }

    public AccountStock(Account account, Stock stock, StockTradeType tradeType, Long balance, Long price) {
        this.account = account;
        this.stock = stock;
        this.tradeType = tradeType;
        this.balance = balance;
        this.price = price;
    }

    public AccountStock(Long id, Account account, Stock stock, StockTradeType tradeType, Long balance, Long price) {
        this(account, stock, tradeType, balance, price);
        this.id = id;
    }

    public Long id() {
        return id;
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

    public StockTradeType tradeType() {
        return tradeType;
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

    public Long totalAmount() {
        return balance * price *
                ((tradeType == StockTradeType.SELLING) ? -1 : 1);
    }

    public Long totalCount() {
        return balance * ((tradeType == StockTradeType.SELLING) ? -1 : 1);
    }
}
