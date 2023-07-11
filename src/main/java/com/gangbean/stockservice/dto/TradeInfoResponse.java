package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.Trade;
import com.gangbean.stockservice.domain.TradeType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class TradeInfoResponse {

    private Long id;

    private AccountInfoResponse account;

    private TradeType tradeType;

    private LocalDateTime tradeAt;

    private Long amount;

    public TradeInfoResponse(Long id, AccountInfoResponse account, TradeType tradeType, LocalDateTime tradeAt, Long amount) {
        this.id = id;
        this.account = account;
        this.tradeType = tradeType;
        this.tradeAt = tradeAt;
        this.amount = amount;
    }

    public static TradeInfoResponse responseOf(Trade trade) {
        return new TradeInfoResponse(trade.id(), AccountInfoResponse.responseOf(trade.fromWhat()), trade.how(), trade.when(), trade.howMuch());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeInfoResponse that = (TradeInfoResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(account, that.account) && tradeType == that.tradeType && Objects.equals(tradeAt, that.tradeAt) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, tradeType, tradeAt, amount);
    }
}
