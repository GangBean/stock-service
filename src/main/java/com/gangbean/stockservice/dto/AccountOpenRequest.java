package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.Account;
import com.gangbean.stockservice.domain.Bank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountOpenRequest {
    private String bankName;
    private Long bankNumber;
    private Long balance;

    public AccountOpenRequest(String bankName, Long bankNumber, Long balance) {
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.balance = balance;
    }

    public static AccountOpenRequest requestOf(Account account) {
        return new AccountOpenRequest(account.bank().name(), account.bank().number(), account.balance());
    }

    public Account asAccount(Bank bank, String accountNumber) {
        return new Account(accountNumber, bank, balance);
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
}
