package com.gangbean.stockservice.domain;

import com.gangbean.stockservice.exception.AccountStockBuyBelowZeroException;
import com.gangbean.stockservice.exception.AccountStockNotEnoughBalanceException;
import com.gangbean.stockservice.exception.AccountStockSellBelowZeroException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class AccountStock {

    private Long id;
    private Account account;
    private Stock stock;
    private BigDecimal balance;
    private BigDecimal price;
    private BigDecimal totalPaid;

    public AccountStock() {}

    public AccountStock(Long id, Account account, Stock stock, BigDecimal balance, BigDecimal price, BigDecimal totalPaid) {
        this.id = id;
        this.account = account;
        this.stock = stock;
        this.balance = balance;
        this.price = price;
        this.totalPaid = totalPaid;
    }
    public Long id() {
        return id;
    }

    public Account whose() {
        return account;
    }

    public Stock what() {
        return stock;
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

    public BigDecimal howMany() {
        return balance;
    }

    public BigDecimal howMuch() {
        return price;
    }

    public BigDecimal howMuchTotal() {
        return totalPaid;
    }

    public void sell(BigDecimal count) {
        if (count.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountStockSellBelowZeroException("0이하의 개수는 팔 수 없습니다: " + count);
        }
        if (balance.compareTo(count) < 0) {
            throw new AccountStockNotEnoughBalanceException("보유수량이 부족합니다: " + balance);
        }
        balance = balance.subtract(count);
        totalPaid = totalPaid.subtract(price.multiply(count));
    }

    public void buy(BigDecimal price, BigDecimal count) {
        if (count.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountStockBuyBelowZeroException("0이하의 개수는 구매할 수 없습니다: " + count);
        }
        balance = balance.add(count);
        totalPaid = totalPaid.add(price.multiply(count));
        this.price = totalPaid.divide(balance, RoundingMode.DOWN);
    }
}
