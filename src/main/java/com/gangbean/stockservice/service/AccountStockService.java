package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.*;
import com.gangbean.stockservice.dto.StockBuyRequest;
import com.gangbean.stockservice.dto.StockBuyResponse;
import com.gangbean.stockservice.exception.AccountNotExistsException;
import com.gangbean.stockservice.exception.StockNotFoundException;
import com.gangbean.stockservice.repository.AccountRepository;
import com.gangbean.stockservice.repository.AccountStockRepository;
import com.gangbean.stockservice.repository.StockRepository;
import com.gangbean.stockservice.repository.TradeRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    public StockBuyResponse responseOfBuy(StockBuyRequest stockBuyRequest, LocalDateTime buyAt) {
        Stock stock = stockRepository.findById(stockBuyRequest.getStockId())
                .orElseThrow(() -> new StockNotFoundException("요청 ID에 해당하는 주식 정보가 존재하지 않습니다: " + stockBuyRequest.getStockId()));
        Account account = accountRepository.findById(stockBuyRequest.getAccountId())
                .orElseThrow(() -> new AccountNotExistsException("요청 ID에 해당하는 계좌정보가 존재하지 않습니다: " + stockBuyRequest.getAccountId()));
        account.withDraw(stockBuyRequest.getAmount() * stockBuyRequest.getPrice());
        stock.sell(stockBuyRequest.getAmount());

        tradeRepository.save(new Trade(account, TradeType.PAYMENT, buyAt, stockBuyRequest.getAmount()));
        AccountStock accountStock = accountStockRepository.save(new AccountStock(account, stock, StockTradeType.BUYING, stockBuyRequest.getAmount(), stockBuyRequest.getPrice()));

        List<AccountStock> buyList = accountStockRepository.findAllByAccountIdAndStockId(accountStock.stock().id(), accountStock.stock().id());
        return StockBuyResponse.responseOf(accountStock, buyList);
    }
}
