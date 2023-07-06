package com.gangbean.stockservice.service

import com.gangbean.stockservice.domain.Stock
import com.gangbean.stockservice.dto.StockInfoResponse
import com.gangbean.stockservice.repository.StockRepository
import spock.lang.Specification

class StockServiceTest extends Specification {

    StockRepository stockRepository

    StockService stockService

    def setup() {
        stockRepository = Mock()
        stockService = new StockService(stockRepository)
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
