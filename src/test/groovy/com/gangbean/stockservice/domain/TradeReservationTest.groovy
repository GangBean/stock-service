package com.gangbean.stockservice.domain


import com.gangbean.stockservice.exception.account.AccountNotEnoughBalanceException
import com.gangbean.stockservice.exception.TradeReservationCannotAllowBelowZeroAmountException
import com.gangbean.stockservice.exception.TradeReservationOnlyAcceptHourlyBasisTimeException
import spock.lang.Specification

import java.time.LocalDateTime

class TradeReservationTest extends Specification {

    def "결제예약은 계좌의 잔액을 넘는 금액은 거절합니다"() {
        given:
        Long id = 1L
        LocalDateTime tradeAt = LocalDateTime.of(2023, 12, 30, 1, 0)
        Long balance = 1_000L
        Account account = new Account(1L, "1", new Bank("은행", 1L), balance)

        when:
        Long amount = 10_000L
        def reservation = new TradeReservation(id, account, tradeAt, amount)

        then:
        def error = thrown(AccountNotEnoughBalanceException.class)

        expect:
        balance < amount
        error.getMessage() == "계좌잔액을 초과하는 금액은 예약불가합니다: " + balance
    }

    def "결제예약은 계좌를 요구하고 반환합니다"() {
        given:
        Long id = 1L
        LocalDateTime tradeAt = LocalDateTime.of(2023, 12, 30, 1, 0)
        Account account = new Account(1L, "1", new Bank("은행", 1L), 1000L)

        when:
        def reservation = new TradeReservation(id, account, tradeAt, 100L)

        then:
        reservation.from() == account
    }

    def "결제예약은 0이하의 금액은 거절합니다"(Long amount) {
        given:
        Long id = 1L
        LocalDateTime tradeAt = LocalDateTime.of(2023, 12, 30, 1, 0)
        Account account = new Account(1L, "1", new Bank("은행", 1L), 1000L)

        when:
        def reservation = new TradeReservation(id, account, tradeAt, amount)

        then:
        def error = thrown(TradeReservationCannotAllowBelowZeroAmountException.class)

        expect:
        error.getMessage() == "0이하의 금액은 예약불가합니다: " + amount

        where:
        amount << [-100L, -1000L, -1L]
    }

    def "결제예약은 금액을 요구하고 반환합니다"() {
        given:
        Long id = 1L
        LocalDateTime tradeAt = LocalDateTime.of(2023, 12, 30, 1, 0)
        Long amount = 1_000L
        Account account = new Account(1L, "1", new Bank("은행", 1L), 1000L)

        when:
        def reservation = new TradeReservation(id, account, tradeAt, 1_000L)

        then:
        reservation.howMuch() == amount
    }

    def "결제예약은 시간단위의 예약만 허용합니다"(int hour, int minute) {
        given:
        Long id = 1L
        LocalDateTime tradeAt = LocalDateTime.of(2023, 12, 30, hour, minute)
        Account account = new Account(1L, "1", new Bank("은행", 1L), 1000L)

        when:
        new TradeReservation(id, account, tradeAt, 100L)

        then:
        def error = thrown(TradeReservationOnlyAcceptHourlyBasisTimeException.class)

        expect:
        error.getMessage() == "결제예약은 매 시간단위로만 요청가능합니다: " + tradeAt

        where:
        hour | minute
        1    |   1
        10   |   59
    }

    def "결제예약은 예약시간을 요구하고, 반환합니다"() {
        given:
        Long id = 1L
        LocalDateTime tradeAt = LocalDateTime.of(2023, 12, 30, 14, 00)
        Account account = new Account(1L, "1", new Bank("은행", 1L), 1000L)

        when:
        def reservation = new TradeReservation(id, account, tradeAt, 100L)

        then:
        verifyAll {
            reservation.when() == tradeAt
        }
    }

    def "결제에약은 id를 반환합니다"() {
        given:
        Long id = 1L;
        Account account = new Account(1L, "1", new Bank("은행", 1L), 1000L)

        when:
        def reservation = new TradeReservation(id, account, LocalDateTime.of(2023, 7, 1, 14, 0), 100L)

        then:
        reservation.id() == id
    }

    def "결제예약은 id를 요구합니다"() {
        when:
        new TradeReservation(1L, new Account(1L, "1", new Bank("은행", 1L), 1000L), LocalDateTime.of(2023, 7, 1, 14, 0), 100L)

        then:
        noExceptionThrown()
    }
}
