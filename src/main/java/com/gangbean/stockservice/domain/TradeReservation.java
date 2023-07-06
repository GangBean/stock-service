package com.gangbean.stockservice.domain;

import com.gangbean.stockservice.dto.AccountNotEnoughBalanceException;
import com.gangbean.stockservice.exception.TradeReservationCannotAllowBelowZeroAmountException;
import com.gangbean.stockservice.exception.TradeReservationOnlyAcceptHourlyBasisTimeException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TradeReservation {

    public static final long AMOUNT_BASIS = 0L;
    private Long id;
    private Account account;
    private LocalDateTime tradeAt;
    private Long amount;

    public TradeReservation(Long id, Account account, LocalDateTime tradeAt, Long amount) {
        this.id = id;
        this.account = enoughBalanced(account, amount);
        this.tradeAt = hourlyBasis(tradeAt);
        this.amount = aboveZero(amount);
    }

    public Account from() {
        return account;
    }

    public Long howMuch() {
        return amount;
    }

    public LocalDateTime when() {
        return tradeAt;
    }

    public Long id() {
        return id;
    }

    private Long aboveZero(Long amount) {
        if (belowBasis(amount)) {
            throw new TradeReservationCannotAllowBelowZeroAmountException(AMOUNT_BASIS + "이하의 금액은 예약불가합니다: " + amount);
        }
        return amount;
    }

    private boolean belowBasis(Long amount) {
        return amount <= AMOUNT_BASIS;
    }

    private LocalDateTime hourlyBasis(LocalDateTime tradeAt) {
        if (tradeAt.truncatedTo(ChronoUnit.HOURS) != tradeAt) {
            throw new TradeReservationOnlyAcceptHourlyBasisTimeException("결제예약은 매 시간단위로만 요청가능합니다: " + tradeAt);
        }
        return tradeAt;
    }

    private Account enoughBalanced(Account account, Long amount) {
        if (account.balance() < amount) {
            throw new AccountNotEnoughBalanceException("계좌잔액을 초과하는 금액은 예약불가합니다: " + account.balance());
        }
        return account;
    }
}
