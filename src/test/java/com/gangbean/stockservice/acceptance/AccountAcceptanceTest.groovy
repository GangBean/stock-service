package com.gangbean.stockservice.acceptance

import spock.lang.Specification

class AccountAcceptanceTest extends Specification {

    /***
     * given 은행이름과 은행번호, 금액이 주어질때
     * and 금액이 0원 미만이면
     * when 계좌등록 요청시
     * then 401 Bad Request 응답이 반환된다.
     */
    def openAccount_NotEnoughBalance() {

    }

    /**
     * given 은행이름과 은행번호, 금액이 주어질때
     * and 금액이 0원 이상이고
     * and 은행 이름과 은행번호에 해당하는 은행이 존재하지 않으면
     * when 계좌등록 요청시
     * then 401 Bad Request 응답이 반환된다.
     */
    def openAccount_NotValidBank() {

    }

    /**
     * given 은행이름과 은행번호, 금액이 주어질때
     * and 금액이 0원 이상이고
     * and 은행이름과 번호에 해당하는 은행이 존재하면
     * when 게좌등록 요청시
     * then 계좌의 id, 은행명, 계좌번호, 잔액과 201 Created 응답이 반환된다.
     */
    def openAccount_Ok() {

    }
}
