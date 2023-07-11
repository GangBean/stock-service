package com.gangbean.stockservice.acceptance

import com.gangbean.stockservice.SpringBootAcceptanceTest
import com.gangbean.stockservice.jwt.TokenProvider
import com.gangbean.stockservice.repository.AccountRepository
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

@SpringBootAcceptanceTest
class AccountCloseAcceptanceTest extends Specification {

    @LocalServerPort
    int port

    @Autowired
    AccountRepository accountRepository

    @Autowired
    TokenProvider tokenProvider0

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

    /***
     * given 로그인한 상태로 계좌ID를 입력할 때
     * and 해당하는 계좌가 저장소에 없으면
     * when 계좌삭제 요청시
     * then 404 Not found가 응답됩니다
     */
    def closeAccount_NotFound() {
        given:
        def accountId = 3L

        and:
        assert accountRepository.findById(accountId).isEmpty()

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .when()
                .delete("/api/accounts/{id}", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.NOT_FOUND.value()
            response.body().jsonPath().getString("message") == "입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId
        }
    }

    /**
     * given 로그인한 상태로 계좌ID를 입력하고
     * and 해당하는 계좌가 저장소에 존재하고
     * and 로그인한 유저가 계좌의 주인이 아니면
     * when 계좌등록 요청시
     * then 403 Forbidden 응답이 반환된다.
     */
    def closeAccount_NotOwner() {
        given:
        def accountId = 2L

        and:
        accountRepository.findById(accountId).isPresent()

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .when()
                .delete("/api/accounts/{id}", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.FORBIDDEN.value()
            response.body().jsonPath().getString("message") == "해당 계좌의 소유자가 아닙니다: " + accountId
        }
    }

    /**
     * given 로그인한 상태로 계좌ID를 입력하고
     * and 해당계좌가 저장소에 존재하고
     * and 로그인한 유저의 계좌일때
     * when 게좌삭제 요청시
     * then 정상삭제되고 No content가 응답됩니다.
     */
    def closeAccount_Ok() {
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
                .delete("/api/accounts/{id}", accountId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.NO_CONTENT.value()
            accountRepository.findById(accountId).isEmpty()
        }
    }
}
