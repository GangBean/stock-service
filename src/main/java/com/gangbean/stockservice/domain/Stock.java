package com.gangbean.stockservice.domain;

import com.gangbean.stockservice.exception.StockAmountNotValidException;
import com.gangbean.stockservice.exception.StockNotEnoughBalanceException;
import com.gangbean.stockservice.exception.stock.StockBuyForOverCurrentPriceException;
import com.gangbean.stockservice.exception.stock.StockSellForBelowCurrentPriceException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private BigDecimal balance;

    public Stock() {
    }

    public Stock(Long id, String name, BigDecimal price, BigDecimal balance) {
        this(name, price, balance);
        this.id = id;
    }

    public Stock(String name, BigDecimal price, BigDecimal balance) {
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

    public BigDecimal howMuch() {
        return price;
    }

    public BigDecimal howMany() {
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

    public void sell(BigDecimal price, BigDecimal amount) {
        if (this.price.compareTo(price) > 0) {
            throw new StockSellForBelowCurrentPriceException("주식의 현재가격보다 낮은 가격으로 구매할 수 없습니다: " + this.price);
        }
        if (balance.compareTo(amount) < 0) {
            throw new StockNotEnoughBalanceException("주식의 잔량이 부족합니다: " + balance);
        }
        balance = balance.subtract(amount);
    }

    public void buy(BigDecimal price, BigDecimal amount) {
        if (this.price.compareTo(price) < 0) {
            throw new StockBuyForOverCurrentPriceException("주식의 현재가격보다 높은 가격으로 판매할 수 없습니다: " + this.price);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new StockAmountNotValidException("1개 이상만 구매가능합니다: " + amount);
        }
        balance = balance.add(amount);
    }
}
