package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.Account;
import com.gangbean.stockservice.domain.Trade;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AccountDetailInfoResponse {

    private final Long id;
    private final String accountNumber;
    private final String bankName;
    private final Long bankNumber;
    private final Long balance;
    private final List<TradeInfoResponse> trades;

    public AccountDetailInfoResponse(Long id, String accountNumber, String bankName, Long bankNumber, Long balance, List<TradeInfoResponse> trades) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.balance = balance;
        this.trades = trades;
    }

    public static AccountDetailInfoResponse responseOf(Account account, List<Trade> trades) {
        return new AccountDetailInfoResponse(account.id(),
                account.number(),
                account.bank().name(),
                account.bank().number(),
                account.balance(),
                trades.stream().map(TradeInfoResponse::responseOf).collect(Collectors.toList()));
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

    public List<TradeInfoResponse> trades() {
        return trades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDetailInfoResponse that = (AccountDetailInfoResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(bankName, that.bankName) && Objects.equals(bankNumber, that.bankNumber) && Objects.equals(balance, that.balance) && Objects.equals(trades, that.trades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, bankName, bankNumber, balance, trades);
    }
}