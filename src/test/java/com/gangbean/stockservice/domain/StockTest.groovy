package com.gangbean.stockservice.domain

import spock.lang.Specification

class StockTest extends Specification {

    def "주식은 잔량을 요구하고 본인의 잔량을 알려줍니다"() {
        given:
        Long stockId = 1L
        String name = "카카오"
        Long price = 1000L
        Long balance = 100L

        when:
        def stock = new Stock(stockId, name, price, balance)

        then:
        stock.howMany() == balance
    }

    def "주식은 가격을 요구하고 본인의 가격을 돌려줍니다"() {
        given:
        Long stockId = 1L
        String name = "카카오"
        Long price = 1000L

        when:
        def stock = new Stock(stockId, name, price, 100L)

        then:
        stock.howMuch() == price
    }

    def "주식은 이름을 요구하고 본인의 이름을 돌려줍니다"() {
        given:
        Long stockId = 1L
        String name = "카카오"

        when:
        def stock = new Stock(stockId, name, 1000L, 100L)

        then:
        stock.name() == name
    }

    def "주식은 id를 요구하고 본인의 id를 돌려줍니다"() {
        given:
        Long stockId = 1L

        when:
        def stock = new Stock(stockId, "카카오", 1000L, 100L)

        then:
        stock.id() == stockId
    }
}
