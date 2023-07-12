package com.gangbean.stockservice.acceptance

import com.gangbean.stockservice.SpringBootAcceptanceTest
import com.gangbean.stockservice.domain.Account
import com.gangbean.stockservice.repository.AccountRepository
import com.gangbean.stockservice.repository.BankRepository
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

@SpringBootAcceptanceTest
class AccountPaymentAcceptanceTest extends Specification {

    @LocalServerPort
    int port

    @Autowired
    AccountRepository accountRepository

    String token

    String username

    String password

    BigDecimal price

    def setup() {
        RestAssured.port = port
        username = "admin"
        password = "admin"
        def loginResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/api/login")
                .then().log().all()
                .extract()
        token = loginResponse.header("Authorization")
        price = 100
    }

    /**
     * given 결제계좌ID와 금액을 입력하고
     * and 해당하는 결제계좌가 존재하고
     * and 본인의 계좌이고
     * and 계좌의 잔액이 금액보다 많을때
     * when 계좌결제를 요청하면
     * then 201 Created 와 계좌의 결제후 정보가 응답됩니다.
     */
    def "계좌결제_정상"() {
        given:
        def accountId = 1L

        and:
        def account = accountRepository.findById(accountId)
        assert account.isPresent()

        and:
        assert account.get().whose().getUsername() == username

        and:
        assert account.get().balance() >= price

        when:
        def response = RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("price", price))
                .when()
                .post("/api/accounts/{id}/payments", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.CREATED.value()
            response.jsonPath().getLong("accountId") == accountId
            new BigDecimal(response.jsonPath().getString("balance")) == 900
        }
    }

    /**
     * given 결제계좌ID와 금액을 입력하고
     * and 해당하는 결제계좌가 존재하고
     * and 본인의 계좌이고
     * and 게좌에 잔액이 금액보다 적을때
     * when 계좌결제를 요청하면
     * then 400 Bad request 오류가 응답됩니다.
     */
    def "계좌결제_잔액부족"() {
        given:
        def accountId = 1L
        price = 100_000

        and:
        def account = accountRepository.findById(accountId)
        assert account.isPresent()

        and:
        assert account.get().whose().getUsername() == username

        and:
        assert account.get().balance() < price

        when:
        def response = RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("price", price))
                .when()
                .post("/api/accounts/{id}/payments", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.BAD_REQUEST.value()
            response.jsonPath().getString("message") == "계좌 잔액이 부족합니다: " + account.get().balance()
        }
    }

    /**
     * given 결제계좌ID와 금액을 입력하고
     * and 해당하는 결제계좌가 존재하고
     * and 타인의 계좌일때
     * when 계좌결제를 요청하면
     * then 403 Forbidden 오류가 응답됩니다.
     */
    def "계좌결제_타인계좌"() {
        given:
        def accountId = 2L

        and:
        def account = accountRepository.findById(accountId)
        assert account.isPresent()

        and:
        assert account.get().whose().getUsername() != username

        when:
        def response = RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("price", price))
                .when()
                .post("/api/accounts/{id}/payments", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.FORBIDDEN.value()
            response.jsonPath().getString("message") == "본인의 계좌가 아닙니다: " + accountId
        }
    }

    /**
     * given 결제계좌ID와 금액을 입력하고
     * and 해당하는 결제계좌가 존재하지 않을때
     * when 계좌결제를 요청하면
     * then 404 Not Found 오류가 응답됩니다.
     */
    def "계좌결제_미존재계좌"() {
        given:
        def accountId = 100L

        and:
        assert accountRepository.findById(accountId).isEmpty()

        when:
        def response = RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("price", price))
                .when()
                .post("/api/accounts/{id}/payments", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.NOT_FOUND.value()
            response.jsonPath().getString("message") == "입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId
        }
    }
}
