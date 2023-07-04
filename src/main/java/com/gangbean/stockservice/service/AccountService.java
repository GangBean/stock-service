package com.gangbean.stockservice.service;

import com.gangbean.stockservice.dto.AccountInfoListResponse;
import com.gangbean.stockservice.dto.AccountInfoResponse;
import com.gangbean.stockservice.entity.Account;
import com.gangbean.stockservice.exception.AccountNotExistsException;
import com.gangbean.stockservice.repository.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public AccountInfoResponse responseOfAccountCreate(Account account) {
        return AccountInfoResponse.responseOf(accountRepository.save(account));
    }

    public AccountInfoResponse accountFindById(Long id) {
        return AccountInfoResponse.responseOf(accountRepository.findById(id)
                        .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id)));
    }

    public AccountInfoListResponse allAccounts() {
        return AccountInfoListResponse.responseOf(accountRepository.findAll());
    }
}
