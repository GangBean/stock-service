package com.gangbean.stockservice.domain;

import com.gangbean.stockservice.exception.StockNotEnoughBalanceException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private Long balance;

    public Stock() {
    }

    public Stock(Long id, String name, Long price, Long balance) {
        this(name, price, balance);
        this.id = id;
    }

    public Stock(String name, Long price, Long balance) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void sell(Long amount) {
        if (balance < amount) {
            throw new StockNotEnoughBalanceException("주식의 잔량이 부족합니다: " + balance);
        }
        balance -= amount;
    }
}
