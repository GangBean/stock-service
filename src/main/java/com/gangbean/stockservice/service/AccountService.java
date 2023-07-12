package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.Account;
import com.gangbean.stockservice.domain.Bank;
import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.dto.*;
import com.gangbean.stockservice.exception.account.AccountNotExistsException;
import com.gangbean.stockservice.exception.account.TradeBetweenSameAccountsException;
import com.gangbean.stockservice.repository.AccountRepository;
import com.gangbean.stockservice.repository.AccountStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.accountStockRepository = accountStockRepository;
    }

    @Transactional
    public AccountInfoResponse responseOfAccountCreate(Member member, Bank bank, BigDecimal balance) {
        String accountNumber = "1";
        Account saved = accountRepository.save(new Account(accountNumber, member, bank, balance, new HashSet<>()));
        return AccountInfoResponse.responseOf(saved);
    }

    public AccountInfoResponse accountFindById(Long id) {
        return AccountInfoResponse.responseOf(accountRepository.findById(id)
                        .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id)));
    }

    public AccountInfoListResponse allAccounts(Long memberId) {
        return AccountInfoListResponse.responseOf(accountRepository.findAllByMemberUserId(memberId));
    }

    public AccountDetailInfoResponse responseOfAccountDetail(Long id, Member member) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id))
                .ownedBy(member);

        return AccountDetailInfoResponse.responseOf(account);
    }

    @Transactional
    public AccountTransferResponse responseOfTransfer(Member member, Long accountId, String toAccountNumber, LocalDateTime tradeAt, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId))
                .ownedBy(member);

        Account toAccount = accountRepository.findByNumber(toAccountNumber)
                .orElseThrow(() -> new AccountNotExistsException("입력된 계좌번호에 해당하는 계좌가 존재하지 않습니다: " + toAccountNumber));
        if (fromAccount.equals(toAccount)) {
            throw new TradeBetweenSameAccountsException("송금계좌와 수신계좌가 동일할 수 없습니다.");
        }

        fromAccount.withDraw(tradeAt, amount);
        toAccount.deposit(tradeAt, amount);

        return AccountTransferResponse.responseOf(fromAccount.balance());
    }

    @Transactional
    public AccountPaymentResponse responseOfPayment(Member member, Long accountId, LocalDateTime tradeAt, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId))
                .ownedBy(member);
        account.pay(tradeAt, amount);
        return AccountPaymentResponse.responseOf(account);
    }

    @Transactional
    public void close(Long id, Member loginUser) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id))
                .ownedBy(loginUser);

        accountStockRepository.deleteAllByAccountId(id);

        accountRepository.delete(account);
    }
}
