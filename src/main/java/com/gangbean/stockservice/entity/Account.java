package com.gangbean.stockservice.entity;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;

    @OneToOne
    private Bank bank;
    private Long balance;

    public Account() {}

    public Account(String number, Bank bank, Long balance) {
        this.number = number;
        this.bank = bank;
        this.balance = balance;
    }

    public Account(Long id, String number, Bank bank, Long balance) {
        this.id = id;
        this.number = number;
        this.bank = bank;
        this.balance = balance;
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
}
