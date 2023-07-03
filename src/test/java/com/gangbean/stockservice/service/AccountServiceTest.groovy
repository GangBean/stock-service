package com.gangbean.stockservice.service

import com.gangbean.stockservice.entity.Account
import com.gangbean.stockservice.entity.Bank
import com.gangbean.stockservice.repository.AccountRepository
import spock.lang.Specification

class AccountServiceTest extends Specification {

    private AccountService accountService

    private AccountRepository accountRepository

    def setup() {
        accountRepository = Mock()
        accountService = new AccountService(accountRepository)
    }

    def "계좌는 계좌를 저장한 응답을 반환해줍니다"() {
        given:
        String number = "000000000"
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1_000L
        Account account = new Account(1L, number, new Bank(bankName, bankNumber), balance)

        when:
        def response = accountService.responseOfAccountCreate(account)

        then:
        1 * accountRepository.save(account) >> account

        verifyAll {
            response.id() != null
            response.accountNumber() == number
            response.bankName() == bankName
            response.bankNumber() == bankNumber
            response.balance() == balance
        }
    }
}
