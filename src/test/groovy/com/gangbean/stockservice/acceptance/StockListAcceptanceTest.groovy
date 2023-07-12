package com.gangbean.stockservice.acceptance

import com.gangbean.stockservice.SpringBootAcceptanceTest
import com.gangbean.stockservice.repository.StockRepository
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import java.util.stream.Collectors

@SpringBootAcceptanceTest
class StockListAcceptanceTest extends Specification {

    @LocalServerPort
    int port

    @Autowired
    StockRepository stockRepository

    String token

    def setup() {
        RestAssured.port = port
        def loginResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("username", "admin", "password", "admin"))
                .when()
                .post("/api/login")
                .then().log().all()
                .extract()
        token = loginResponse.header("Authorization")
    }

    /**
     * given 로그인한 뒤
     * when 주식목록조회 요청하면
     * then 200 Ok 와 주식목록이 반환됩니다.
     */
    def "주식목록조회_정상"() {
        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .when()
                .get("/api/stocks")
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.OK.value()
            response.jsonPath().getList("stocks.id").containsAll(1, 10)
            response.jsonPath().getList("stocks.stockName").containsAll("천만전자", "백만전자")
            response.jsonPath().getList("stocks.price").stream()
                    .map(BigDecimal::new)
                    .collect(Collectors.toList()).containsAll(new BigDecimal(100), new BigDecimal(100))
        }
    }
}
