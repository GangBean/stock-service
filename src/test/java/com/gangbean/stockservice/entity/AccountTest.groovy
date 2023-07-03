package com.gangbean.stockservice.entity

import spock.lang.Specification

class AccountTest extends Specification {

    def "계좌는 ID와 계좌번호, 은행, 잔액을 알려줍니다"() {
        given:
        Long id = 1L
        String number = "000000000";
        Long bankId = 1L
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1000L

        when:
        Account account = new Account(id, number, new Bank(bankId, bankName, bankNumber), balance)

        then:
        verifyAll {
            account.id() == id
            account.number() == number
            account.bank() == new Bank(bankId, bankName, bankNumber)
            account.balance() == balance
        }
    }

    def "계좌는 계좌번호와 은행, 잔액을 요구합니다"() {
        given:
        String number = "0000000000"
        Long bankId = 1L
        String bankName = "XX은행"
        Long bankNumber = 1L
        Long balance = 1000L

        when:
        new Account(number, new Bank(bankId, bankName, bankNumber), balance)

        then:
        noExceptionThrown()
    }
}
