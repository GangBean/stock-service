package com.gangbean.stockservice.service

import com.gangbean.stockservice.domain.*
import com.gangbean.stockservice.dto.StockSellRequest
import com.gangbean.stockservice.repository.AccountRepository
import com.gangbean.stockservice.repository.AccountStockTradeRepository
import com.gangbean.stockservice.repository.StockRepository
import spock.lang.Specification

import java.time.LocalDateTime

import static com.gangbean.stockservice.domain.MemberTest.TEST_MEMBER

class AccountStockTradeServiceTest extends Specification {

    AccountStockTradeService accountStockService

    AccountRepository accountRepository

    StockRepository stockRepository

    AccountStockTradeRepository accountStockRepository

    def setup() {
        accountStockRepository = Mock()
        stockRepository = Mock()
        accountRepository = Mock()
        accountStockService = new AccountStockTradeService(accountRepository
                , stockRepository
                , accountStockRepository)
    }

    def "계좌주식 서비스는 기존에 구매한 양보다 많은 양의 주식판매 요청을 거절합니다"() {

    }

    def "계좌주식 서비스는 주식판매요청을 받아 주식판매를 진행하고, 기존 구매를 반영한 주식판매결과를 반환해줍니다"() {
        given:
        Long accountId = 1L
        Account account = new Account(accountId, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        Long stockId = 1L
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockId, stockName, price, balance)
        Long sellAmount = 5L
        Long sellPrice = 9_950L
        Long boughtAmount = 15L
        Long boughtPrice = 9_000L
        def request = new StockSellRequest(stockId, accountId, sellAmount, sellPrice)
        def newBuy = new AccountStockTrade(1L, account, stock, StockTradeType.BUYING, sellAmount, sellPrice)
        def bought = new AccountStockTrade(2L, account, stock, StockTradeType.SELLING, boughtAmount, boughtPrice)

        when:
        def response = accountStockService.responseOfSell(request, LocalDateTime.now())

        then:
        1 * stockRepository.findById(stockId) >> Optional.of(stock)
        1 * accountRepository.findById(accountId) >> Optional.of(account)
        1 * accountStockRepository.save(_) >> newBuy
        1 * accountStockRepository.findAllByAccountIdAndStockId(accountId, stockId) >> List.of(newBuy, bought)

        verifyAll {
            response.getStockId() == stockId
            response.getAmount() == sellAmount
            account.balance() == 1_049_750L
            stock.howMany() == 105L
            response.getAveragePrice() == 8_525L
        }
    }

    def "계좌주식 서비스는 주식구매요청을 받아 주식구매를 진행하고, 기존 구매를 반영한 주식구매결과를 반환해줍니다"() {
        given:
        Long accountId = 1L
        Account account = new Account(accountId, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        Long stockId = 1L
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockId, stockName, price, balance)
        Long newBuyAmount = 10L
        Long newBuyPrice = 10_050L
        Long boughtAmount = 5L
        Long boughtPrice = 9_000L
        def newBuy = new AccountStockTrade(1L, account, stock, StockTradeType.BUYING, newBuyAmount, newBuyPrice)
        def bought = new AccountStockTrade(2L, account, stock, StockTradeType.BUYING, boughtAmount, boughtPrice)

        when:
        def response = accountStockService.responseOfBuy(TEST_MEMBER, accountId, stockId, newBuyAmount, newBuyPrice, LocalDateTime.now())

        then:
        1 * stockRepository.findById(stockId) >> Optional.of(stock)
        1 * accountRepository.findById(accountId) >> Optional.of(account)
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
