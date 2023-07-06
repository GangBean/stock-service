package com.gangbean.stockservice.repository

import com.gangbean.stockservice.domain.Stock
import com.gangbean.stockservice.domain.StockHistory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
class StockHistoryRepositoryTest extends Specification {

    @Autowired
    StockHistoryRepository stockHistoryRepository

    @Autowired
    StockRepository stockRepository

    def "주식이력저장소는 입력된 주식ID에 해당하는 주식이력들을 반환해줍니다"() {
        given:
        String stockName = "카카오"
        Long stockPrice = 1_000L
        Long stockBalance = 100L
        def stock = stockRepository.save(new Stock(stockName, stockPrice, stockBalance))

        LocalDateTime when = LocalDateTime.of(2023,7,1,14,0)
        Long priorPrice = 900L
        def history = new StockHistory(stock, when, priorPrice)
        def saved = stockHistoryRepository.save(history)
        def history2 = new StockHistory(stock, when.plusHours(1), 1_100L)
        def saved2 = stockHistoryRepository.save(history2)

        when:
        def histories = stockHistoryRepository.findAllByStockId(stock.id())

        then:
        verifyAll {
            histories.size() == 2
            histories.containsAll(saved, saved2)
        }
    }

    def "주식이력저장소는 주식이력을 저장하고, 저장된 주식이력을 반환해줍니다"() {
        given:
        String stockName = "카카오"
        Long stockPrice = 1_000L
        Long stockBalance = 100L
        def stock = stockRepository.save(new Stock(stockName, stockPrice, stockBalance))

        LocalDateTime when = LocalDateTime.of(2023,7,1,14,0)
        Long priorPrice = 900L
        def history = new StockHistory(stock, when, priorPrice)

        when:
        def saved = stockHistoryRepository.save(history)

        then:
        verifyAll {
            saved.id() != null
            saved.ofWho() == stock
            saved.when() == when
            saved.howMuch() == priorPrice
        }
    }
}
