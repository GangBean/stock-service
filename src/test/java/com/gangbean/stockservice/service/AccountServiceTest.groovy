package com.gangbean.stockservice.service

import com.gangbean.stockservice.dto.AccountInfoListResponse
import com.gangbean.stockservice.dto.AccountInfoResponse
import com.gangbean.stockservice.entity.Account
import com.gangbean.stockservice.entity.Bank
import com.gangbean.stockservice.exception.AccountNotExistsException
import com.gangbean.stockservice.repository.AccountRepository
import spock.lang.Specification

class AccountServiceTest extends Specification {

    private AccountService accountService

    private AccountRepository accountRepository

    def setup() {
        accountRepository = Mock()
        accountService = new AccountService(accountRepository)
    }

    def "계좌 서비스는 요청시 계좌목록조회응답을 반환합니다"() {
        given:
        Long id = 1L
        String number = "000000000"
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1_000L
        Account account = new Account(id, number, new Bank(bankName, bankNumber), balance)
        Account account2 = new Account(2L, number, new Bank(bankName, bankNumber), balance)

        when:
        AccountInfoListResponse response = accountService.allAccounts()

        then:
        1 * accountRepository.findAll() >> List.of(account, account2)

        verifyAll {
            response.accountInfoList().size() == 2
            response.accountInfoList().containsAll(AccountInfoResponse.responseOf(account)
                    , AccountInfoResponse.responseOf(account2))
        }
    }

    def "계좌 서비스는 입력된 id에 해당하는 계좌가 없을때 잘못되었다고 알려줍니다"() {
        given:
        Long id = 1L

        when:
        accountService.accountFindById(id)

        then:
        1 * accountRepository.findById(id) >> Optional.empty()
        AccountNotExistsException e = thrown()
        e.getMessage() == "입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + id
    }

    def "계좌 서비스는 입력된 id에 해당하는 계좌 정보를 반환해줍니다"() {
        given:
        Long id = 1L
        String number = "000000000"
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1_000L
        Account account = new Account(id, number, new Bank(bankName, bankNumber), balance)

        when:
        AccountInfoResponse response = accountService.accountFindById(id)

        then:
        1 * accountRepository.findById(id) >> Optional.of(account)

        verifyAll {
            response.id() == id
            response.accountNumber() == number
            response.bankName() == bankName
            response.bankNumber() == bankNumber
            response.balance() == balance
        }
    }

    def "계좌 서비스는 계좌를 저장한 응답을 반환해줍니다"() {
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
