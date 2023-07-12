package com.gangbean.stockservice.domain

import com.gangbean.stockservice.exception.AccountStockBuyBelowZeroException
import com.gangbean.stockservice.exception.AccountStockNotEnoughBalanceException
import com.gangbean.stockservice.exception.AccountStockSellBelowZeroException
import spock.lang.Specification

class AccountStockTest extends Specification {

    Account account = new Account(1L, "00001", MemberTest.TEST_MEMBER, new Bank(1L, "은행", 1L), 1000L, new HashSet<>())
    Stock stock = new Stock(1L, "카카오", 1000L, 100L)
    AccountStock accountStock
    Long id
    BigDecimal balance
    BigDecimal averagePrice
    BigDecimal total

    def setup() {
        id = 1L
        balance = 100
        averagePrice = 1_000
        total = 100_000
        accountStock = new AccountStock(id, account, stock, balance, averagePrice, total)
    }

    def "계좌주식은 0이하의 개수는 구매할 수 없습니다"(BigDecimal count) {
        given:
        BigDecimal price = 1_000

        when:
        accountStock.buy(price, count)

        then:
        def error = thrown(AccountStockBuyBelowZeroException.class)

        expect:
        error.getMessage() == "0이하의 개수는 구매할 수 없습니다: " + count

        where:
        count << [-100, -1, 0]
    }

    def "계좌주식은 입력한 가격과 개수만큼 구매해, 잔량과 총액을 늘리고, 평균금액을 조정합니다"() {
        given:
        BigDecimal count = 10
        BigDecimal price = 10_000

        when:
        accountStock.buy(price, count)

        then:
        verifyAll {
            accountStock.howMany() == 110
            accountStock.howMuchTotal() == 200_000
            accountStock.howMuch() == 1_818
        }
    }

    def "계좌주식은 보유한 수량을 초과해 팔 수 없습니다"(BigDecimal count) {
        when:
        accountStock.sell(count)

        then:
        def error = thrown(AccountStockNotEnoughBalanceException.class)

        expect:
        count > accountStock.howMany()
        error.getMessage() == "보유수량이 부족합니다: " + balance

        where:
        count << [101, 1_000, 10_000]
    }

    def "계좌주식은 0이하의 개수는 팔 수 없습니다"(BigDecimal count) {
        when:
        accountStock.sell(count)

        then:
        def error = thrown(AccountStockSellBelowZeroException.class)

        expect:
        error.getMessage() == "0이하의 개수는 팔 수 없습니다: " + count

        where:
        count << [-1000, 0]
    }

    def "계좌주식은 입력된 개수만큼 팔아 보유량과 총액을 줄이고, 평균금액은 유지합니다"() {
        when:
        BigDecimal sellCount = 10
        accountStock.sell(sellCount)

        then:
        accountStock.howMany() == 90
        accountStock.howMuchTotal() == 90_000
        accountStock.howMuch() == 1_000
    }

    def "계좌주식은 본인의 평균금액을 알려줍니다"() {
        when:
        BigDecimal howMuch = accountStock.howMuch()

        then:
        howMuch == averagePrice
    }

    def "계좌주식은 본인의 총금액을 알려줍니다"() {
        when:
        Double howMuchTotal = accountStock.howMuchTotal()

        then:
        howMuchTotal == total
    }

    def "계좌주식은 보유량을 요구하고 본인의 보유량을 알려줍니다"() {
        when:
        def howMany = accountStock.howMany()

        then:
        howMany == balance
    }

    def "게좌주식은 주식을 요구하고 본인의 주식을 알려줍니다"() {
        given:
        def accountStockId = 1L

        when:
        def what = accountStock.what()

        then:
        noExceptionThrown()
        what == stock
    }

    def "게좌주식은 계좌를 요구하고 본인의 계좌를 알려줍니다"() {
        when:
        def whose = accountStock.whose()

        then:
        noExceptionThrown()
        whose == account
    }

    def "계좌주식은 id가 같으면 동등합니다"() {
        when:
        def clone = new AccountStock(1L, null, null, null, null, null)

        then:
        noExceptionThrown()
        accountStock == clone
    }

    def "게좌주식은 id를 요구하고 반환합니다"() {
        when:
        def accountStockId = 1L

        then:
        noExceptionThrown()
        accountStock.id() == accountStockId
    }
}
