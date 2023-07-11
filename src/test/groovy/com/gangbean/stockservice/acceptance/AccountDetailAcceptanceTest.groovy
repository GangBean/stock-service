package com.gangbean.stockservice.acceptance

import com.gangbean.stockservice.SpringBootAcceptanceTest
import com.gangbean.stockservice.jwt.TokenProvider
import com.gangbean.stockservice.repository.AccountRepository
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

@SpringBootAcceptanceTest
class AccountDetailAcceptanceTest extends Specification {
    @LocalServerPort
    int port

    @Autowired
    AccountRepository accountRepository

    @Autowired
    TokenProvider tokenProvider

    String token

    String username

    String password

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
    }

    /**
     * given 로그인한 상태로 계좌 ID를 입력하고
     * and 해당하는 계좌가 존재하지 않을때
     * when 계좌 상세 조회 요청을 하면
     * then 404 Not found 응답이 반환됩니다.
     */
    def detailOfNotExists() {
        given:
        def accountId = 3L

        and:
        def account = accountRepository.findById(accountId)
        assert account.isEmpty()

        when:
        def response = RestAssured.given().log().all()
                .header("Authorization", token)
                .when()
                .get("/api/accounts/{id}", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.NOT_FOUND.value()
            response.jsonPath().getString("message") == "입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId
        }
    }

    /**
     * given 로그인한 상태로 계좌 ID를 입력하고
     * and 해당하는 계좌가 존재하고
     * and 로그인한 사용자가 계좌 주인이 아닐 때
     * when 계좌 상세 조회 요청을 하면
     * then 403 Forbidden 응답이 반환됩나다.
     */
    def detailOfOthers() {
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
                .when()
                .get("/api/accounts/{id}", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.FORBIDDEN.value()
            response.jsonPath().getString("message") == "해당 계좌의 소유자가 아닙니다: " + accountId
        }
    }

    /**
     * given 로그인한 상태로 계좌 ID를 입력하고
     * and 로그인한 사용자가 게좌 주인일 때
     * when 계좌 상세 조회 요청을 하면
     * then 200 Ok 응답과 계좌 거래내역을 포함한 상세내역이 반환됩니다.
     */
    def detailOfMine() {
        given:
        def accountId = 1L

        and:
        def account = accountRepository.findById(accountId)
        assert account.isPresent()

        and:
        assert account.get().whose().getUsername() == username

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .when()
                .get("/api/accounts/{id}", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.OK.value()
            response.jsonPath().getString("accountNumber") == "1234"
            response.jsonPath().getString("bankName") == "은행"
            response.jsonPath().getLong("balance") == 1_000L
            response.jsonPath().getList("trades.id", Long.class).containsAll(1L, 2L, 3L)
        }
    }
}
