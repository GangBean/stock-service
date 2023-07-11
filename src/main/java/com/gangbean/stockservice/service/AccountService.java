package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.*;
import com.gangbean.stockservice.dto.*;
import com.gangbean.stockservice.exception.account.AccountNotExistsException;
import com.gangbean.stockservice.exception.account.AccountNotOwnedByLoginUser;
import com.gangbean.stockservice.repository.AccountRepository;
import com.gangbean.stockservice.repository.TradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final TradeRepository tradeRepository;

    public AccountService(AccountRepository accountRepository, TradeRepository tradeRepository) {
        this.accountRepository = accountRepository;
        this.tradeRepository = tradeRepository;
    }

    public AccountInfoResponse responseOfAccountCreate(AccountOpenRequest account, Member member, Bank bank) {
        String accountNumber = "1";
        return AccountInfoResponse.responseOf(accountRepository.save(account.asAccount(accountNumber, member, bank)));
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

    @Transactional
    public void close(Long id, Member loginUser) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id));

        if (!account.isOwner(loginUser)) {
            throw new AccountNotOwnedByLoginUser("해당 계좌의 소유자가 아닙니다: " + id);
        }

        accountRepository.delete(account);
    }
}
