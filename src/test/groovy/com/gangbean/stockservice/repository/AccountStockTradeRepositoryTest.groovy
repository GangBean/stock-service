package com.gangbean.stockservice.repository

import com.gangbean.stockservice.DataIsolationTest
import com.gangbean.stockservice.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import static com.gangbean.stockservice.domain.MemberTest.TEST_MEMBER

@DataIsolationTest
@DataJpaTest
class AccountStockTradeRepositoryTest extends Specification {

    @Autowired
    AccountStockTradeRepository accountStockRepository

    @Autowired
    AccountRepository accountRepository

    @Autowired
    BankRepository bankRepository

    @Autowired
    StockRepository stockRepository

    def "계좌주식 저장소는 입력한 계좌ID와 주식ID가 일치하는 계좌주식들을 반환해줍니다"() {
        given:
        def bank = bankRepository.save(new Bank("은행", 1L))
        def account = accountRepository.save(new Account("0", TEST_MEMBER, bank, 1_000_000L, new HashSet<>()))
        def stock = stockRepository.save(new Stock("카카오", 10_000L, 100L))
        def stock2 = stockRepository.save(new Stock("현대차", 18_000L, 100L))
        Long balance = 10L
        Long price = 5_000L
        def accountStock = new AccountStockTrade(account, stock, StockTradeType.BUYING, balance, price)
        def accountStock2 = new AccountStockTrade(account, stock, StockTradeType.BUYING, balance, price)
        def accountStock3 = new AccountStockTrade(account, stock2, StockTradeType.BUYING, balance, price)
        def saved = accountStockRepository.save(accountStock)
        def saved2 = accountStockRepository.save(accountStock2)
        def saved3 = accountStockRepository.save(accountStock3)

        when:
        def accountStockList = accountStockRepository.findAllByAccountIdAndStockId(account.id(), stock.id())

        then:
        verifyAll {
            accountStockList.size() == 2
            accountStockList.containsAll(saved, saved2)
            !accountStockList.contains(saved3)
        }
    }

    def "계좌주식 저장소는 계좌주식을 저장하고, 저장한 계좌주식을 돌려줍니다"() {
        given:
        def bank = bankRepository.save(new Bank("은행", 1L))
        def account = accountRepository.save(new Account("0", TEST_MEMBER, bank, 1_000_000L, new HashSet<>()))
        def stock = stockRepository.save(new Stock("카카오", 10_000L, 100L))
        Long balance = 10L
        Long price = 5_000L
        def accountStock = new AccountStockTrade(account, stock, StockTradeType.BUYING, balance, price)

        when:
        def saved = accountStockRepository.save(accountStock)

        then:
        verifyAll {
            saved.id() != null
            saved.amount() == balance
            saved.price() == price
        }
    }
}
