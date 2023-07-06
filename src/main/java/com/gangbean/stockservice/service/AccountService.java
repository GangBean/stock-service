package com.gangbean.stockservice.service;

import com.gangbean.stockservice.dto.*;
import com.gangbean.stockservice.domain.Account;
import com.gangbean.stockservice.domain.Trade;
import com.gangbean.stockservice.domain.TradeType;
import com.gangbean.stockservice.exception.AccountNotExistsException;
import com.gangbean.stockservice.repository.AccountRepository;
import com.gangbean.stockservice.repository.TradeRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public class AccountService {

    private final AccountRepository accountRepository;

    private final TradeRepository tradeRepository;

    public AccountService(AccountRepository accountRepository, TradeRepository tradeRepository) {
        this.accountRepository = accountRepository;
        this.tradeRepository = tradeRepository;
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

    public AccountDetailInfoResponse accountFindByIdWithTrades(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id));
        List<Trade> trades = tradeRepository.findAllByAccountId(id);
        return AccountDetailInfoResponse.responseOf(account, trades);
    }

    @Transactional
    public AccountTransferResponse responseOfTransfer(Long id, String toAccountNumber, LocalDateTime tradeAt, Long amount) {
        Account fromAccount = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id));
        Account toAccount = accountRepository.findByNumber(toAccountNumber)
                .orElseThrow(() -> new AccountNotExistsException("입력된 계좌번호에 해당하는 계좌가 존재하지 않습니다: " + toAccountNumber));
        fromAccount.withDraw(amount);
        toAccount.deposit(amount);
        tradeRepository.save(new Trade(fromAccount, TradeType.WITHDRAW, tradeAt, amount));
        tradeRepository.save(new Trade(toAccount, TradeType.DEPOSIT, tradeAt, amount));
        return AccountTransferResponse.responseOf(fromAccount.balance());
    }

    @Transactional
    public AccountPaymentResponse responseOfPayment(Long id, LocalDateTime tradeAt, Long amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id));
        account.withDraw(amount);
        tradeRepository.save(new Trade(account, TradeType.PAYMENT, tradeAt, amount));
        return AccountPaymentResponse.responseOf(account.balance());
    }
}
