package com.gangbean.stockservice.dto

import com.gangbean.stockservice.domain.Account
import com.gangbean.stockservice.domain.Bank
import spock.lang.Specification

class AccountSaveRequestTest extends Specification {
    def "계좌저장요청은 계좌를 전달받아 계좌요청저장으로 반환해줍니다" () {
        given:
        Long bankNumber = 1L
        String bankName = "은행"
        String accountNumber = "1"
        Long balance = 100L
        Account account = new Account(accountNumber, new Bank(bankName, bankNumber), balance)

        when:
        def request = AccountSaveRequest.requestOf(account)

        then:
        verifyAll {
            request.bankNumber() == bankNumber
            request.bankName() == bankName
            request.balance() == balance
        }
    }

    def "계좌저장요청은 잔액을 요구하고 알려줍니다"() {
        given:
        Long bankNumber = 1L
        Long balance = 100L

        when:
        def request = new AccountSaveRequest("은행", bankNumber, balance)

        then:
        request.balance() == balance
    }

    def "계좌저장요청은 은행번호를 요구하고 알려줍니다"() {
        given:
        Long bankNumber = 1L

        when:
        def request = new AccountSaveRequest("은행", bankNumber, 100L)

        then:
        request.bankNumber() == bankNumber
    }

    def "계좌저장요청은 은행이름을 요구하고 알려줍니다"() {
        given:
        String bankName = "은행"

        when:
        def request = new AccountSaveRequest(bankName, 1L, 100L)

        then:
        request.bankName() == bankName
    }
}
