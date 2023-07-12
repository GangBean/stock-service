package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.domain.StockHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class StockHistoryInfoResponse {
    private Long price;

    private LocalDateTime createdAt;

    public StockHistoryInfoResponse(Long price, LocalDateTime createdAt) {
        this.price = price;
        this.createdAt = createdAt;
    }

    public static StockHistoryInfoResponse responseOf(StockHistory stockHistory) {
        return new StockHistoryInfoResponse(stockHistory.howMuch(), stockHistory.when());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockHistoryInfoResponse that = (StockHistoryInfoResponse) o;
        return Objects.equals(price, that.price) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, createdAt);
    }
}
