package com.gangbean.stockservice.repository

import com.gangbean.stockservice.entity.Account
import com.gangbean.stockservice.entity.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class AccountRepositoryTest extends Specification {

    @Autowired
    private AccountRepository accountRepository

    @Autowired
    private BankRepository bankRepository

    String bankName = "은행"
    Long bankNumber = 1L
    Bank bank
    Long bankId

    def setup() {
        bank = bankRepository.save(new Bank(bankName, bankNumber))
        bankId = bank.id()
    }

    def cleanup() {
        bankRepository.deleteAll()
    }

    def "계좌 리포지토리는 계좌정보를 저장합니다"() {
        given:
        String number = "000000000";
        Long balance = 1000L
        Account account = new Account(number, bank, balance)

        when:
        def saved = accountRepository.save(account)

        then:
        verifyAll {
            saved.id() != null
            saved.number() == number
            saved.bank() == new Bank(bankId, bankName, bankNumber)
            saved.balance() == balance
        }
    }
}