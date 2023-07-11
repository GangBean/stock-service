package com.gangbean.stockservice.acceptance

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TradeAcceptanceTest extends Specification {

    /**
     * given 로그인한 상태로 계좌ID, 수신계좌번호, 금액을 입력하고
     * and 금액이 0 초과이고
     * and 해당하는 계좌가 존재하고
     * and 계좌의 주인이고
     * and 수신계좌번호에 해당하는 계좌가 존재하면
     * when 송금요청시
     * then 201 Created 응답이 반환됩니다.
     */

    /**
     * given 로그인한 상태로 계좌ID, 수신계좌번호, 금액을 입력하고
     * and 금액이 0 초과이고
     * and 해당하는 계좌가 존재하고
     * and 계좌의 주인이고
     * and 수신계좌번호에 해당하는 계좌가 존재하지 않으면
     * when 송금요청시
     * then 404 Not Found 응답이 반환됩니다.
     */

    /**
     * given 로그인한 상태로 계좌ID, 수신계좌번호, 금액을 입력하고
     * and 금액이 0 초과이고
     * and 해당하는 계좌가 존재하고
     * and 계좌의 주인이 아니면
     * when 송금요청시
     * then 403 Forbidden 응답이 반환됩니다.
     */

    /**
     * given 로그인한 상태로 계좌ID, 수신계좌번호, 금액을 입력하고
     * and 금액이 0 초과이고
     * and 해당하는 계좌가 존재하지 않으면
     * when 송금요청시
     * then 404 Not Found 응답이 반환됩니다.
     */

    /**
     * given 로그인한 상태로 계좌ID, 수신계좌번호, 금액을 입력하고
     * and 금액이 0이하이면
     * when 송금요청시
     * then 401 Bad Request 응답이 반환됩니다.
     */

}
