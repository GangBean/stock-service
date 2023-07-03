package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.entity.Bank;

public class BankInfoResponse {

    private final Long id;

    private final String name;

    private final Long number;

    public BankInfoResponse(Long id, String name, Long number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Long number() {
        return number;
    }

    public static BankInfoResponse responseOf(Bank bank) {
        return new BankInfoResponse(bank.id(), bank.name(), bank.number());
    }
}