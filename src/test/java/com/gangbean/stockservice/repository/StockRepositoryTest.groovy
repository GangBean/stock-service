package com.gangbean.stockservice.repository

import com.gangbean.stockservice.domain.Stock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class StockRepositoryTest extends Specification {

    @Autowired
    StockRepository stockRepository

    def "주식 저장소는 저장된 주식전체를 돌려줍니다"() {
        given:
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockName, price, balance)
        def saved = stockRepository.save(stock)

        String stockName2 = "네이버"
        Long price2 = 15_000L
        Long balance2 = 150L
        Stock stock2 = new Stock(stockName2, price2, balance2)
        def saved2 = stockRepository.save(stock2)

        when:
        def stocks = stockRepository.findAll()

        then:
        verifyAll {
            stocks.size() == 2
            stocks.containsAll(saved, saved2)
        }
    }

    def "주식 저장소는 주식을 저장하고 저장된 정보를 돌려줍니다"() {
        given:
        String stockName = "카카오"
        Long price = 10_000L
        Long balance = 100L
        Stock stock = new Stock(stockName, price, balance)

        when:
        def saved = stockRepository.save(stock)

        then:
        verifyAll {
            saved.id() != null
            saved.name() == stockName
            saved.howMuch() == price
            saved.howMany() == balance
        }
    }
}
