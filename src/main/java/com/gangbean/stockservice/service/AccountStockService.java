package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.Account;
import com.gangbean.stockservice.domain.AccountStock;
import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.domain.Stock;
import com.gangbean.stockservice.dto.StockBuyResponse;
import com.gangbean.stockservice.dto.StockSellResponse;
import com.gangbean.stockservice.exception.accountstock.AccountStockNotExistsException;
import com.gangbean.stockservice.exception.StockNotFoundException;
import com.gangbean.stockservice.exception.account.AccountNotExistsException;
import com.gangbean.stockservice.repository.AccountRepository;
import com.gangbean.stockservice.repository.AccountStockRepository;
import com.gangbean.stockservice.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

@Transactional(readOnly = true)
@Service
public class AccountStockService {

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;

    public AccountStockService(AccountRepository accountRepository
            , StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    @Transactional
    public StockBuyResponse responseOfBuy(Member member, Long accountId, Long stockId, BigDecimal amount, BigDecimal price, LocalDateTime buyAt) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId))
                .ownedBy(member);

        Stock marketStock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException("입력된 ID에 해당하는 주식이 존재하지 않습니다: " + stockId));

        AccountStock accountStock = accountStockRepository.findByAccountIdAndStockId(accountId, stockId)
                .orElse(new AccountStock(account, marketStock, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new HashSet<>()));

        accountStock.buy(price, amount, buyAt);
        marketStock.sell(price, amount);
        account.withDraw(buyAt, price.multiply(amount));

        return StockBuyResponse.responseOf(accountStock);
    }

    @Transactional
    public StockSellResponse responseOfSell(Member member, Long accountId, Long stockId, BigDecimal amount, BigDecimal price, LocalDateTime sellAt) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId))
                .ownedBy(member);

        Stock marketStock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException("입력된 ID에 해당하는 주식이 존재하지 않습니다: " + stockId));

        AccountStock accountStock = accountStockRepository.findByAccountIdAndStockId(accountId, stockId)
                .orElseThrow(() -> new AccountStockNotExistsException("보유한 주식이 아닙니다: " + stockId));

        accountStock.sell(price, amount, sellAt);
        marketStock.buy(price, amount);
        account.deposit(sellAt, amount.multiply(price));

        return StockSellResponse.responseOf(accountStock);
    }
}
