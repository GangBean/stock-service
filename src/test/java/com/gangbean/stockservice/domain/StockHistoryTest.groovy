package com.gangbean.stockservice.domain

import spock.lang.Specification

import java.time.LocalDateTime

class StockHistoryTest extends Specification {
    def "주식이력은 가격을 요구하고, 본인이 가진 가격을 돌려줍니다"() {
        given:
        Long stockId = 1L
        String stockName = "카카오"
        Long stockPrice = 1_000L
        Long stockBalance = 100L
        def stock = new Stock(stockId, stockName, stockPrice, stockBalance)
        LocalDateTime when = LocalDateTime.of(2023,7,1,14,0)
        Long priorPrice = 900L

        when:
        def history = new StockHistory(stock, when, priorPrice)

        then:
        history.howMuch() == priorPrice
    }

    def "주식이력은 생성시간을 요구하고, 본인이 가진 생성시간을 돌려줍니다"() {
        given:
        Long stockId = 1L
        String stockName = "카카오"
        Long stockPrice = 1_000L
        Long stockBalance = 100L
        def stock = new Stock(stockId, stockName, stockPrice, stockBalance)
        LocalDateTime when = LocalDateTime.of(2023,7,1,14,0)

        when:
        def history = new StockHistory(stock, when, 100L)

        then:
        history.when() == when
    }

    def "주식이력은 주식을 요구하고, 본인이 가진 주식을 돌려줍니다"() {
        given:
        Long stockId = 1L
        String stockName = "카카오"
        Long stockPrice = 1_000L
        Long stockBalance = 100L
        def stock = new Stock(stockId, stockName, stockPrice, stockBalance)
        LocalDateTime when = LocalDateTime.of(2023,7,1,14,0)

        when:
        def history = new StockHistory(stock, when, 100L)

        then:
        history.ofWho() == stock
    }
}
