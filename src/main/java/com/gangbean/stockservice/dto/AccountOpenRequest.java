package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.Account;
import com.gangbean.stockservice.domain.Bank;
import com.gangbean.stockservice.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountOpenRequest {
    private String bankName;
    private Long bankNumber;
    private Long balance;
    private Long memberId;

    public AccountOpenRequest(String bankName, Long bankNumber, Long balance, Long memberId) {
        this.bankName = bankName;
        this.bankNumber = bankNumber;
        this.balance = balance;
        this.memberId = memberId;
    }

    public static AccountOpenRequest requestOf(Account account) {
        return new AccountOpenRequest(account.bank().name(), account.bank().number(), account.balance(), account.whose().getUserId());
    }

    public Account asAccount(String accountNumber, Member member, Bank bank) {
        return new Account(accountNumber, member, bank, balance);
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
