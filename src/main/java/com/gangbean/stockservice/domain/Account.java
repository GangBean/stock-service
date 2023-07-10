package com.gangbean.stockservice.domain;

import com.gangbean.stockservice.exception.account.AccountNotEnoughBalanceException;
import com.gangbean.stockservice.exception.account.AccountCannotDepositBelowZeroAmountException;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @ManyToOne
    private Member member;
    @OneToOne
    private Bank bank;
    private Long balance;

    public Account() {}

    public Account(String number, Member member, Bank bank, Long balance) {
        this.number = number;
        this.member = member;
        this.bank = bank;
        this.balance = moreThanZero(balance);
    }

    public Account(Long id, String number, Member member, Bank bank, Long balance) {
        this(number, member, bank, balance);
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public String number() {
        return number;
    }

    public Bank bank() {
        return bank;
    }

    public Long balance() {
        return balance;
    }

    public void deposit(Long amount) {
        if (amount <= 0L) {
            throw new AccountCannotDepositBelowZeroAmountException("계좌는 0원 이하 금액을 입금할 수 없습니다: " + amount);
        }
        balance += amount;
    }

    public void withDraw(Long amount) {
        if (amount > balance) {
            throw new AccountNotEnoughBalanceException("계좌 잔액이 부족합니다: " + balance);
        }
        balance -= amount;
    }

    private Long moreThanZero(Long balance) {
        if (balance < 0) {
            throw new AccountNotEnoughBalanceException("0원 미만의 금액은 입금할 수 없습니다: " + balance);
        }
        return balance;
    }

    public Member whose() {
        return member;
    }
}
