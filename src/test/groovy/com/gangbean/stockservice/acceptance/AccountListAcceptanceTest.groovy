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
class AccountListAcceptanceTest extends Specification {

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
     * given 로그인 한 상태로
     * when 게좌 목록 조회 요청하면
     * then 로그인한 사용자의 계좌 목록이 조회됩니다.
     */
    def accountList() {
        when:
        def response = RestAssured.given().log().all()
                .header("Authorization", token)
                .when()
                .get("/api/accounts")
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.OK.value()
            response.jsonPath().getList("accounts.accountNumber", String.class).contains("1234")
            !response.jsonPath().getList("accounts.accountNumber", String.class).contains("1235")
        }
    }
}
