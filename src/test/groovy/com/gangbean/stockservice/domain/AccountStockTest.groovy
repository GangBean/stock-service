package com.gangbean.stockservice.domain


import spock.lang.Specification

import static com.gangbean.stockservice.domain.MemberTest.*

class AccountStockTest extends Specification {
    def "계좌주식은 같은 ID를 가지면 동등합니다"() {
        given:
        def account = new Account(1L, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L
        Long id = 1L

        when:
        def accountStock = new AccountStock(id, account, stock, StockTradeType.BUYING, balance, averagePrice)

        then:
        accountStock == new AccountStock(id, account, stock, StockTradeType.BUYING, 100L, 1_000L)
    }

    def "계좌주식은 ID를 요청하고, 자신의 ID를 반환합니다"() {
        given:
        def account = new Account(1L, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L
        Long id = 1L

        when:
        def accountStock = new AccountStock(id, account, stock, StockTradeType.BUYING, balance, averagePrice)

        then:
        accountStock.id() == id
    }

    def "계좌주식은 금액을 요청하고, 자신의 금액을 반환합니다"() {
        given:
        def account = new Account(1L, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long price = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, StockTradeType.BUYING, balance, price)

        then:
        accountStock.price() == price
    }

    def "계좌주식은 잔량을 요청하고, 자신의 잔량을 반환합니다"() {
        given:
        def account = new Account(1L, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, StockTradeType.BUYING, balance, averagePrice)

        then:
        accountStock.balance() == balance
    }

    def "계좌주식은 주식정보를 요청하고, 자신의 주식정보를 반환합니다"() {
        given:
        def account = new Account(1L, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, StockTradeType.BUYING, balance, averagePrice)

        then:
        accountStock.stock() == stock
    }

    def "계좌주식은 계좌정보를 요청하고, 자신의 계좌정보를 반환합니다"() {
        given:
        def account = new Account(1L, "0", TEST_MEMBER, new Bank(1L, "은행", 1L), 1_000_000L, new HashSet<>())
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, StockTradeType.BUYING, balance, averagePrice)

        then:
        accountStock.account() == account
    }
}
