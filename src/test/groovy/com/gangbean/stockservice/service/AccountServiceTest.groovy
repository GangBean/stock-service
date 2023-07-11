package com.gangbean.stockservice.service

import com.gangbean.stockservice.domain.Account
import com.gangbean.stockservice.domain.Bank
import com.gangbean.stockservice.domain.Trade
import com.gangbean.stockservice.domain.TradeType
import com.gangbean.stockservice.dto.AccountInfoListResponse
import com.gangbean.stockservice.dto.AccountInfoResponse
import com.gangbean.stockservice.dto.TradeInfoResponse
import com.gangbean.stockservice.exception.account.AccountNotExistsException
import com.gangbean.stockservice.repository.AccountRepository
import spock.lang.Specification

import java.time.LocalDateTime

import static com.gangbean.stockservice.domain.MemberTest.TEST_MEMBER

class AccountServiceTest extends Specification {

    private AccountService accountService

    private AccountRepository accountRepository

    def setup() {
        accountRepository = Mock()
        accountService = new AccountService(accountRepository)
    }

    def "계좌 서비스는 결제계좌 ID와 금액을 입력하면 결제를 진행하고, 거래정보를 만들고, 계좌잔액을 돌려줍니다"() {
        given:
        Long id = 1L
        String number = "000000000"
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1_000L
        Account account = new Account(id, number, TEST_MEMBER, new Bank(bankName, bankNumber), balance, new HashSet<>())

        and:
        def amount = 100L
        def tradeAt = LocalDateTime.now()

        when:
        def response = accountService.responseOfPayment(id, tradeAt, amount)

        then:
        1 * accountRepository.findById(id) >> Optional.of(account)
        verifyAll {
            response.balance() == 900L
        }
    }

    def "계좌 서비스는 송금계좌 ID 계좌로부터 수신게좌번호 계좌로 입력한 금액만큼 송금하고, 거래정보를 만들고, 계좌잔액을 돌려줍니다"() {
        given:
        Long id = 1L
        String number = "000000000"
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1_000L
        Account account = new Account(id, number, TEST_MEMBER, new Bank(bankName, bankNumber), balance, new HashSet<>())

        and:
        Long toAccountId = 2L
        String toAccountNumber = "1111"
        Account toAccount = new Account(toAccountId, toAccountNumber, TEST_MEMBER, new Bank(bankName, bankNumber), balance, new HashSet<>())

        and:
        def amount = 100L
        def tradeAt = LocalDateTime.now()

        when:
        def response = accountService.responseOfTransfer(TEST_MEMBER, id, toAccountNumber, tradeAt, amount)

        then:
        1 * accountRepository.findById(id) >> Optional.of(account)
        1 * accountRepository.findByNumber(toAccountNumber) >> Optional.of(toAccount)

        verifyAll {
            response.balance() == 900L
        }
    }

    def "계좌 서비스는 전달한 계좌 ID에 해당하는 계좌정보를 거래정보와 함께 돌려옵니다"() {
        given:
        Long id = 1L
        String number = "000000000"
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1_000L

        def tradeId = 1L
        def tradeType = TradeType.DEPOSIT
        def amount = 1_000L
        def tradeAt = LocalDateTime.of(2023,07,05,14,20)
        def trade = new Trade(tradeId, tradeType, tradeAt, amount)

        Account account = new Account(id, number, TEST_MEMBER, new Bank(bankName, bankNumber), balance, new HashSet<>(Set.of(trade)))

        when:
        def response = accountService.responseOfAccountDetail(id, TEST_MEMBER)

        then:
        1 * accountRepository.findById(id) >> Optional.of(account)

        verifyAll {
            response.id() == id
            response.accountNumber() == number
            response.bankName() == bankName
            response.bankNumber() == bankNumber
            response.balance() == balance
            response.trades().contains(TradeInfoResponse.responseOf(trade))
        }
    }

    def "계좌 서비스는 요청시 계좌목록조회응답을 반환합니다"() {
        given:
        Long id = 1L
        String number = "000000000"
        String bankName = "은행"
        Long bankNumber = 1L
        Long balance = 1_000L
        Account account = new Account(id, number, TEST_MEMBER, new Bank(bankName, bankNumber), balance, new HashSet<>())
        Account account2 = new Account(2L, number, TEST_MEMBER, new Bank(bankName, bankNumber), balance, new HashSet<>())

        when:
        AccountInfoListResponse response = accountService.allAccounts(TEST_MEMBER.getUserId())

        then:
        1 * accountRepository.findAllByMemberUserId(TEST_MEMBER.getUserId()) >> List.of(account, account2)

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
        Account account = new Account(id, number, TEST_MEMBER, new Bank(bankName, bankNumber), balance, new HashSet<>())

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
        def bank = new Bank(bankName, bankNumber)
        Account account = new Account(1L, number, TEST_MEMBER, bank, balance, new HashSet<>())

        when:
        def response = accountService.responseOfAccountCreate(TEST_MEMBER, bank, balance)

        then:
        1 * accountRepository.save(_) >> account

        verifyAll {
            response.id() != null
            response.accountNumber() == number
            response.bankName() == bankName
            response.bankNumber() == bankNumber
            response.balance() == balance
        }
    }
}
