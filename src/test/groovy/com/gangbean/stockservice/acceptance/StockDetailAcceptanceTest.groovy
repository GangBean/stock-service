package com.gangbean.stockservice.acceptance

import com.gangbean.stockservice.SpringBootAcceptanceTest
import com.gangbean.stockservice.repository.StockRepository
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import java.util.stream.Collectors

@SpringBootAcceptanceTest
class StockDetailAcceptanceTest extends Specification{

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
     * given 주식 ID를 입력하고
     * and 해당하는 주식이 존재하면
     * when 주식상세정보 조회요청시
     * then 200 Ok와 주식의 과거 가격정보를 포함한 주식 상세정보가 반환됩니다.
     */
    def "주식상세조회_정상"() {
        given:
        def stockId = 1L

        and:
        assert stockRepository.findById(stockId)
                .isPresent()

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .when()
                .get("/api/stocks/{id}", stockId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.OK.value()
            response.jsonPath().getLong("stockId") == stockId
            response.jsonPath().getString("stockName") == "백만전자"
            response.jsonPath().getList("histories.price").stream()
                    .map(BigDecimal::new)
                    .collect(Collectors.toList()).containsAll(new BigDecimal(80), new BigDecimal(90))
        }
    }

    /**
     * given 주식 ID를 입력하고
     * and 해당하는 주식이 존재하지 않을때
     * when 주식상세정보 조회요청을 하면
     * then 404 Not Found 응답이 반환됩니다.
     */
    def "주식상세조회_미존재주식"() {
        given:
        def stockId = 1010L

        and:
        assert stockRepository.findById(stockId)
                .isEmpty()

        when:
        def response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", token)
                .when()
                .get("/api/stocks/{id}", stockId)
                .then().log().all()
                .extract()

        then:
        verifyAll {
            response.statusCode() == HttpStatus.NOT_FOUND.value()
            response.jsonPath().getString("message") == "입력된 ID에 해당하는 주식이 존재하지 않습니다: " + stockId
        }
    }

}
