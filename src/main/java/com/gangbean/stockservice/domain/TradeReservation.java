package com.gangbean.stockservice.domain;

import com.gangbean.stockservice.dto.AccountNotEnoughBalanceException;
import com.gangbean.stockservice.exception.TradeReservationCannotAllowBelowZeroAmountException;
import com.gangbean.stockservice.exception.TradeReservationOnlyAcceptHourlyBasisTimeException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
public class TradeReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private LocalDateTime tradeAt;

    private Long amount;

    public TradeReservation() {}

    public TradeReservation(Account account, LocalDateTime tradeAt, Long amount) {
        this.account = enoughBalanced(account, amount);
        this.tradeAt = hourlyBasis(tradeAt);
        this.amount = aboveZero(amount);
    }

    public TradeReservation(Long id, Account account, LocalDateTime tradeAt, Long amount) {
        this(account, tradeAt, amount);
        this.id = id;
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
            throw new TradeReservationCannotAllowBelowZeroAmountException("0이하의 금액은 예약불가합니다: " + amount);
        }
        return amount;
    }

    private boolean belowBasis(Long amount) {
        return amount <= 0L;
    }

    private LocalDateTime hourlyBasis(LocalDateTime tradeAt) {
        if (isNotHourlyBasis(tradeAt)) {
            throw new TradeReservationOnlyAcceptHourlyBasisTimeException("결제예약은 매 시간단위로만 요청가능합니다: " + tradeAt);
        }
        return tradeAt;
    }

    private static boolean isNotHourlyBasis(LocalDateTime tradeAt) {
        return tradeAt.truncatedTo(ChronoUnit.HOURS) != tradeAt;
    }

    private Account enoughBalanced(Account account, Long amount) {
        if (account.balance() < amount) {
            throw new AccountNotEnoughBalanceException("계좌잔액을 초과하는 금액은 예약불가합니다: " + account.balance());
        }
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeReservation that = (TradeReservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
