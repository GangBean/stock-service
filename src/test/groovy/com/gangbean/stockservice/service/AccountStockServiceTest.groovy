package com.gangbean.stockservice.service

import com.gangbean.stockservice.domain.*
import com.gangbean.stockservice.dto.StockBuyRequest
import com.gangbean.stockservice.dto.StockSellRequest
import com.gangbean.stockservice.repository.AccountRepository
import com.gangbean.stockservice.repository.AccountStockRepository
import com.gangbean.stockservice.repository.StockRepository
import com.gangbean.stockservice.repository.TradeRepository
import com.gangbean.stockservice.service.AccountStockService
import spock.lang.Specification

import java.time.LocalDateTime

class AccountStockServiceTest extends Specification {

    AccountStockService accountStockService

    AccountRepository accountRepository

    StockRepository stockRepository

    AccountStockRepository accountStockRepository

    TradeRepository tradeRepository

    def setup() {
        accountStockRepository = Mock()
        stockRepository = Mock()
        accountRepository = Mock()
        tradeRepository = Mock()
        accountStockService = new AccountStockService(accountRepository
                , stockRepository
                , accountStockRepository
                , tradeRepository)
    }

    def "계좌주식 서비스는 기존에 구매한 양보다 많은 양의 주식판매 요청을 거절합니다"() {

    }

    def "계좌주식 서비스는 주식판매요청을 받아 주식판매를 진행하고, 기존 구매를 반영한 주식판매결과를 반환해줍니다"() {
        given:
        Long accountId = 1L
        Account account = new Account(accountId, "0", new Bank(1L, "은행", 1L), 1_000_000L)
        Long stockId = 1L
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockId, stockName, price, balance)
        Long sellAmount = 5L
        Long sellPrice = 10_050L
        Long boughtAmount = 15L
        Long boughtPrice = 9_000L
        def request = new StockSellRequest(stockId, accountId, sellAmount, sellPrice)
        def newBuy = new AccountStock(1L, account, stock, StockTradeType.BUYING, sellAmount, sellPrice)
        def bought = new AccountStock(2L, account, stock, StockTradeType.SELLING, boughtAmount, boughtPrice)

        when:
        def response = accountStockService.responseOfSell(request, LocalDateTime.now())

        then:
        1 * stockRepository.findById(stockId) >> Optional.of(stock)
        1 * accountRepository.findById(accountId) >> Optional.of(account)
        1 * tradeRepository.save(_)
        1 * accountStockRepository.save(_) >> newBuy
        1 * accountStockRepository.findAllByAccountIdAndStockId(accountId, stockId) >> List.of(newBuy, bought)

        verifyAll {
            response.getStockId() == stockId
            response.getAmount() == sellAmount
            account.balance() == 1_050_250L
            stock.howMany() == 105L
            response.getAveragePrice() == 8475L
        }
    }

    def "계좌주식 서비스는 주식구매요청을 받아 주식구매를 진행하고, 기존 구매를 반영한 주식구매결과를 반환해줍니다"() {
        given:
        Long accountId = 1L
        Account account = new Account(accountId, "0", new Bank(1L, "은행", 1L), 1_000_000L)
        Long stockId = 1L
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockId, stockName, price, balance)
        Long newBuyAmount = 10L
        Long newBuyPrice = 10_050L
        Long boughtAmount = 5L
        Long boughtPrice = 9_000L
        def request = new StockBuyRequest(stockId, accountId, newBuyAmount, newBuyPrice)
        def newBuy = new AccountStock(1L, account, stock, StockTradeType.BUYING, newBuyAmount, newBuyPrice)
        def bought = new AccountStock(2L, account, stock, StockTradeType.BUYING, boughtAmount, boughtPrice)

        when:
        def response = accountStockService.responseOfBuy(request, LocalDateTime.now())

        then:
        1 * stockRepository.findById(stockId) >> Optional.of(stock)
        1 * accountRepository.findById(accountId) >> Optional.of(account)
        1 * tradeRepository.save(_)
        1 * accountStockRepository.save(_) >> newBuy
        1 * accountStockRepository.findAllByAccountIdAndStockId(accountId, stockId) >> List.of(newBuy, bought)

        verifyAll {
            response.getStockId() == stockId
            response.getAmount() == newBuyAmount
            stock.howMany() == 90L
            account.balance() == 899_500L
            response.getAveragePrice() == 9_700L
        }
    }
}
