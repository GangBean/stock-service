package com.gangbean.stockservice.acceptance

import com.gangbean.stockservice.SetTestData
import com.gangbean.stockservice.repository.BankRepository
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

@SetTestData
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountOpenAcceptanceTest extends Specification {

    @LocalServerPort
    int port

    @Autowired
    BankRepository bankRepository

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
     * given 은행이름과 은행번호, 금액이 주어질 때
     * and 은행이름과 번호에 해당하는 은행이 저장소에 존재하고
     * and 금액이 0원 미만이면
     * when 계좌등록 요청시
     * then 401 Bad Request 응답이 반환된다.
     */
    def openAccount_NotEnoughBalance() {
        given:
        def bankName = "은행"
        def bankNumber = "1"
        def balance = -1_000L
        def param = Map.of(
                "bankName", bankName,
                "bankNumber", bankNumber,
                "balance", balance)

        and:
        bankRepository.findByNameAndNumber(bankName, Long.parseLong(bankNumber)).isPresent()

        and:
        balance < 0

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .header("Authorization", token)
                .when()
                .post("/api/accounts")

        then:
        verifyAll {
            response.statusCode() == HttpStatus.BAD_REQUEST.value()
            response.body().jsonPath().getString("message") == "0원 미만의 금액은 입금할 수 없습니다: " + balance
        }
    }

    /**
     * given 은행이름과 은행번호, 금액이 주어질때
     * and 은행 이름과 은행번호에 해당하는 은행이 저장소에 존재하지 않으면
     * when 계좌등록 요청시
     * then 401 Bad Request 응답이 반환된다.
     */
    def openAccount_NotValidBank() {
        given:
        def bankName = "은행"
        def bankNumber = "2"
        def balance = 1_000L
        def param = Map.of(
                "bankName", bankName,
                "bankNumber", bankNumber,
                "balance", balance)

        and:
        bankRepository.findByNameAndNumber(bankName, Long.parseLong(bankNumber)).isEmpty()

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .body(param)
                .when()
                .post("/api/accounts").then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.BAD_REQUEST.value()
            response.jsonPath().getString("message") == "은행 이름과 번호에 해당하는 은행이 존재하지 않습니다: " + bankName + " / " + bankNumber
        }
    }


    /**
     * given 은행이름과 은행번호, 금액이 주어질때
     * and 금액이 0원 이상이고
     * and 은행이름과 번호에 해당하는 은행이 저장소에 존재하면
     * when 게좌등록 요청시
     * then 계좌의 id, 은행명, 계좌번호, 잔액과 201 Created 응답이 반환된다.
     */
    def openAccount_Ok() {
        given:
        def bankName = "은행"
        def bankNumber = "1"
        def balance = 1_000L
        def param = Map.of(
                "bankName", bankName,
                "bankNumber", bankNumber,
                "balance", balance)

        and:
        bankRepository.findByNameAndNumber(bankName, Long.parseLong(bankNumber)).isPresent()

        and:
        balance >= 0

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .body(param)
                .when()
                .post("/api/accounts")

        then:
        verifyAll {
            response.statusCode() == HttpStatus.CREATED.value()
            response.body().jsonPath().getString("id") != null
            response.body().jsonPath().getString("bankName") == bankName
            response.body().jsonPath().getString("accountNumber") != null
            response.body().jsonPath().getLong("balance") == balance
        }
    }
}
