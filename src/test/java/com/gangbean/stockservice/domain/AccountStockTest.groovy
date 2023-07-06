package com.gangbean.stockservice.domain

import spock.lang.Specification

class AccountStockTest extends Specification {
    def "계좌주식은 평균금액을 요청하고, 자신의 평균금액을 반환합니다"() {
        given:
        def account = new Account(1L, "0", new Bank(1L, "은행", 1L), 1_000_000L)
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, balance, averagePrice)

        then:
        accountStock.averagePrice() == averagePrice
    }

    def "계좌주식은 잔량을 요청하고, 자신의 잔량을 반환합니다"() {
        given:
        def account = new Account(1L, "0", new Bank(1L, "은행", 1L), 1_000_000L)
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, balance, averagePrice)

        then:
        accountStock.balance() == balance
    }

    def "계좌주식은 주식정보를 요청하고, 자신의 주식정보를 반환합니다"() {
        given:
        def account = new Account(1L, "0", new Bank(1L, "은행", 1L), 1_000_000L)
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, balance, averagePrice)

        then:
        accountStock.stock() == stock
    }

    def "계좌주식은 계좌정보를 요청하고, 자신의 계좌정보를 반환합니다"() {
        given:
        def account = new Account(1L, "0", new Bank(1L, "은행", 1L), 1_000_000L)
        def stock = new Stock(1L, "카카오", 10_000L, 100L)
        Long balance = 10L
        Long averagePrice = 5_000L

        when:
        def accountStock = new AccountStock(account, stock, balance, averagePrice)

        then:
        accountStock.account() == account
    }
}
