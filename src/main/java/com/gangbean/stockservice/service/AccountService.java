package com.gangbean.stockservice.service;

import com.gangbean.stockservice.dto.AccountInfoResponse;
import com.gangbean.stockservice.entity.Account;
import com.gangbean.stockservice.repository.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public AccountInfoResponse responseOfAccountCreate(Account account) {
        return AccountInfoResponse.responseOf(accountRepository.save(account));
    }
}
