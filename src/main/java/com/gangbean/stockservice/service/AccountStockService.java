package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.*;
import com.gangbean.stockservice.dto.StockBuyResponse;
import com.gangbean.stockservice.dto.StockSellRequest;
import com.gangbean.stockservice.dto.StockSellResponse;
import com.gangbean.stockservice.exception.StockNotFoundException;
import com.gangbean.stockservice.exception.account.AccountNotExistsException;
import com.gangbean.stockservice.exception.account.AccountNotOwnedByLoginUser;
import com.gangbean.stockservice.repository.AccountRepository;
import com.gangbean.stockservice.repository.AccountStockRepository;
import com.gangbean.stockservice.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class AccountStockService {

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;

    public AccountStockService(AccountRepository accountRepository
            , StockRepository stockRepository
            , AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    @Transactional
    public StockBuyResponse responseOfBuy(Member member, Long accountId, Long stockId, Long amount, Long price, LocalDateTime buyAt) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId));
        if (!account.isOwner(member)) {
            throw new AccountNotOwnedByLoginUser("본인의 계좌가 아닙니다: " + accountId);
        }

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException("입력된 ID에 해당하는 주식이 존재하지 않습니다: " + stockId));

        stock.sell(price, amount);

        account.withDraw(buyAt, price * amount);

        AccountStockTrade accountStockTrade = accountStockRepository.save(new AccountStockTrade(account, stock, StockTradeType.BUYING, amount, price));

        List<AccountStockTrade> stockTradeList = accountStockRepository.findAllByAccountIdAndStockId(accountId, stockId);
        return StockBuyResponse.responseOf(accountStockTrade, stockTradeList);
    }

    @Transactional
    public StockSellResponse responseOfSell(StockSellRequest stockSellRequest, LocalDateTime sellAt) {

        Stock stock = stockRepository.findById(stockSellRequest.getStockId())
                .orElseThrow(() -> new StockNotFoundException("요청 ID에 해당하는 주식 정보가 존재하지 않습니다: " + stockSellRequest.getStockId()));
        Account account = accountRepository.findById(stockSellRequest.getAccountId())
                .orElseThrow(() -> new AccountNotExistsException("요청 ID에 해당하는 계좌정보가 존재하지 않습니다: " + stockSellRequest.getAccountId()));
        account.deposit(sellAt,stockSellRequest.getAmount() * stockSellRequest.getPrice());
        stock.buy(stockSellRequest.getPrice(), stockSellRequest.getAmount());

        AccountStockTrade accountStockTrade = accountStockRepository.save(new AccountStockTrade(account, stock, StockTradeType.SELLING, stockSellRequest.getAmount(), stockSellRequest.getPrice()));

        List<AccountStockTrade> stockTradeList = accountStockRepository.findAllByAccountIdAndStockId(accountStockTrade.stock().id(), accountStockTrade.stock().id());
        return StockSellResponse.responseOf(accountStockTrade, stockTradeList);
    }
}
