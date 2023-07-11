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
import com.gangbean.stockservice.repository.TradeRepository;
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

    private final TradeRepository tradeRepository;

    public AccountStockService(AccountRepository accountRepository
            , StockRepository stockRepository
            , AccountStockRepository accountStockRepository
            , TradeRepository tradeRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.tradeRepository = tradeRepository;
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

        account.withDraw(price * amount);
        tradeRepository.save(new Trade(account, TradeType.PAYMENT, buyAt, price * amount));

        AccountStock accountStock = accountStockRepository.save(new AccountStock(account, stock, StockTradeType.BUYING, amount, price));

        List<AccountStock> stockTradeList = accountStockRepository.findAllByAccountIdAndStockId(accountId, stockId);
        return StockBuyResponse.responseOf(accountStock, stockTradeList);
    }

    public StockSellResponse responseOfSell(StockSellRequest stockSellRequest, LocalDateTime sellAt) {

        Stock stock = stockRepository.findById(stockSellRequest.getStockId())
                .orElseThrow(() -> new StockNotFoundException("요청 ID에 해당하는 주식 정보가 존재하지 않습니다: " + stockSellRequest.getStockId()));
        Account account = accountRepository.findById(stockSellRequest.getAccountId())
                .orElseThrow(() -> new AccountNotExistsException("요청 ID에 해당하는 계좌정보가 존재하지 않습니다: " + stockSellRequest.getAccountId()));
        account.deposit(stockSellRequest.getAmount() * stockSellRequest.getPrice());
        stock.buy(stockSellRequest.getPrice(), stockSellRequest.getAmount());

        tradeRepository.save(new Trade(account, TradeType.DEPOSIT, sellAt, stockSellRequest.getAmount()));
        AccountStock accountStock = accountStockRepository.save(new AccountStock(account, stock, StockTradeType.SELLING, stockSellRequest.getAmount(), stockSellRequest.getPrice()));

        List<AccountStock> stockTradeList = accountStockRepository.findAllByAccountIdAndStockId(accountStock.stock().id(), accountStock.stock().id());
        return StockSellResponse.responseOf(accountStock, stockTradeList);
    }
}
