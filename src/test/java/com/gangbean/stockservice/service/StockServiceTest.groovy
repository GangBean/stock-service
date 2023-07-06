package com.gangbean.stockservice.service

import com.gangbean.stockservice.domain.Account
import com.gangbean.stockservice.domain.Bank
import com.gangbean.stockservice.domain.Stock
import com.gangbean.stockservice.domain.StockHistory
import com.gangbean.stockservice.dto.StockBuyRequest
import com.gangbean.stockservice.dto.StockHistoryInfoResponse
import com.gangbean.stockservice.dto.StockInfoResponse
import com.gangbean.stockservice.repository.AccountRepository
import com.gangbean.stockservice.repository.StockHistoryRepository
import com.gangbean.stockservice.repository.StockRepository
import spock.lang.Specification

import java.time.LocalDateTime
import java.util.stream.Collectors

class StockServiceTest extends Specification {

    StockRepository stockRepository

    StockService stockService

    StockHistoryRepository stockHistoryRepository

    AccountRepository accountRepository

    def setup() {
        stockRepository = Mock()
        stockHistoryRepository = Mock()
        accountRepository = Mock()
        stockService = new StockService(stockRepository, stockHistoryRepository, accountRepository)
    }

    def "주식 서비스는 주식구매요청을 받아 구매결과를 반환해줍니다"() {
        given:
        String accountId = 1L
        Account account = new Account(accountId, "0", new Bank(1L, "은행", 1L), 1_000_000L  )
        Long stockId = 1L
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockId, stockName, price, balance)
        Long buyAmount = 10L
        Long buyPrice = 10_050L
        def request = new StockBuyRequest(stockId, accountId, buyAmount, buyPrice)

        when:
        def response = stockService.responseOfBuy(request)

        then:
        1 * stockRepository.findById(stockId) >> Optional.of(stock)
        1 * accountRepository.findById(accountId) >> Optional.of(account)

        verifyAll {
            response.getStockId() == stockId
            response.getAmount() == stockName
        }
    }

    def "주식 서비스는 입력된 id에 해당하는 주식과 해당주식의 이력을 응답형태로 반환해줍니다"() {
        given:
        Long stockId = 1L
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockId, stockName, price, balance)
        LocalDateTime when = LocalDateTime.of(2023,7,1,14,0)
        Long priorPrice = 900L
        def history = new StockHistory(stock, when, priorPrice)
        def history2 = new StockHistory(stock, when.plusHours(2), 1_200L)

        when:
        def response = stockService.responseOfStockDetail(stockId)

        then:
        1 * stockRepository.findById(stockId) >> Optional.of(stock)
        1 * stockHistoryRepository.findAllByStockId(_) >> List.of(history, history2)
        def histories = List.of(history, history2).stream().map(StockHistoryInfoResponse::responseOf).collect(Collectors.toList())

        verifyAll {
            response.getStockId() == stockId
            response.getStockName() == stockName
            response.getHistories() == histories
        }
    }

    def "주식 서비스는 주식전체 데이터를 응답형태로 반환해줍니다"() {
        given:
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(1L, stockName, price, balance)

        String stockName2 = "네이버"
        Long price2 = 15_000L
        Long balance2 = 150L
        Stock stock2 = new Stock(2L, stockName2, price2, balance2)

        when:
        def allStock = stockService.respondsOfAllStock();

        then:
        1 * stockRepository.findAll() >> List.of(stock, stock2)
        verifyAll {
            allStock.containsAll(
                    StockInfoResponse.responseOf(stock),
                    StockInfoResponse.responseOf(stock2)
            )
        }
    }
}
