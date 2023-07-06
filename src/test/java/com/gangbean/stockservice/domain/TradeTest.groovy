package com.gangbean.stockservice.domain

import spock.lang.Specification

import java.time.LocalDateTime

class TradeTest extends Specification {
    public static final Account TEST_ACCOUNT = new Account(1L, "00000", new Bank(1L, "은행", 1L), 1_000L)
    public static final Account TEST_ACCOUNT2 = new Account(2L, "00100", new Bank(1L, "은행", 1L), 1_000L)
    public static final Trade TRADE = new Trade(1L, TEST_ACCOUNT, TradeType.DEPOSIT, LocalDateTime.now(), 1_000L)
    public static final Trade TRADE2 = new Trade(2L, TEST_ACCOUNT2, TradeType.WITHDRAW, LocalDateTime.now(), 1_000L)

    def "거래는 본인의 식별자를 알려줍니다"() {
        given:
        Long id = 1L
        Long amount = 1_000L
        LocalDateTime tradeAt = LocalDateTime.of(2023,07,01,14,0)
        TradeType type = TradeType.DEPOSIT
        Account account = new Account(1L, "00000", new Bank(1L, "은행", 1L), 1_000L)

        when:
        Trade trade = new Trade(id, account, type, tradeAt, amount)

        then:
        trade.id() == id
    }

    def "거래는 거래가 이루어진 계좌를 알려줍니다"() {
        given:
        Long amount = 1_000L
        LocalDateTime tradeAt = LocalDateTime.of(2023,07,01,14,0)
        TradeType type = TradeType.DEPOSIT
        Account account = new Account(1L, "00000", new Bank(1L, "은행", 1L), 1_000L)

        when:
        Trade trade = new Trade(1L, account, type, tradeAt, amount)

        then:
        trade.fromWhat() == account
    }

    def "거래는 어떻게 이루어졌는지 알려줍니다"() {
        given:
        Long amount = 1_000L
        LocalDateTime tradeAt = LocalDateTime.of(2023,07,01,14,0)
        TradeType type = TradeType.DEPOSIT
        Account account = new Account(1L, "00000", new Bank(1L, "은행", 1L), 1_000L)

        when:
        Trade trade = new Trade(1L, account, type, tradeAt, amount)

        then:
        trade.how() == type
    }

    def "거래는 얼마나 주고받았는지 알려줍니다"() {
        given:
        Long amount = 1_000L
        LocalDateTime tradeAt = LocalDateTime.of(2023,07,01,14,0)
        Account account = new Account(1L, "00000", new Bank(1L, "은행", 1L), 1_000L)

        when:
        Trade trade = new Trade(1L, account, TradeType.WITHDRAW, tradeAt, amount)

        then:
        trade.howMuch() == amount
    }

    def "거래는 언제 거래가 이뤄졌는지 알려줍니다" () {
        given:
        LocalDateTime tradeAt = LocalDateTime.of(2023,07,01,14,0)
        Account account = new Account(1L, "00000", new Bank(1L, "은행", 1L), 1_000L)

        when:
        Trade trade = new Trade(1L, account, TradeType.PAYMENT, tradeAt, 1_000L)

        then:
        trade.when() == tradeAt
    }
}
