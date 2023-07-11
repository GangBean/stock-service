package com.gangbean.stockservice.repository

import com.gangbean.stockservice.SetTestData
import com.gangbean.stockservice.domain.Account
import com.gangbean.stockservice.domain.Trade
import com.gangbean.stockservice.domain.TradeType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.time.LocalDateTime

import static com.gangbean.stockservice.domain.MemberTest.TEST_MEMBER
import static com.gangbean.stockservice.domain.TradeTest.TEST_ACCOUNT

@SetTestData
@DataJpaTest
class TradeRepositoryTest extends Specification {

    @Autowired
    private TradeRepository tradeRepository

    @Autowired
    private BankRepository bankRepository

    @Autowired
    private AccountRepository accountRepository

    @Sql("/initialize.sql")
    def setup() {
        System.out.println(">>>>>>>>>>")
    }

    def "거래 리포지토리는 계좌에 해당하는 거래를 반환합니다"() {
        given:
        def bank = bankRepository.save(TEST_ACCOUNT.bank())
        def account = new Account("0", TEST_MEMBER, bank, 1000L)
        def account2 = new Account("1", TEST_MEMBER, bank, 1500L)
        accountRepository.save(account)
        accountRepository.save(account2)
        def trade = new Trade(1L, account, TradeType.WITHDRAW, LocalDateTime.now(), 100L)
        def trade2 = new Trade(2L, account2, TradeType.WITHDRAW, LocalDateTime.now(), 200L)
        tradeRepository.save(trade)
        tradeRepository.save(trade2)

        when:
        def trades = tradeRepository.findAllByAccountId(account.id())

        then:
        verifyAll {
            trades.size() == 1
            trades.contains(trade)
            !trades.contains(trade2)
        }
    }
}
