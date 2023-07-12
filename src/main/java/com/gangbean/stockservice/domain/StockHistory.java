package com.gangbean.stockservice.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class StockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime writtenAt;
    private Long price;

    public StockHistory() {}

    public StockHistory(LocalDateTime writtenAt, Long price) {
        this.writtenAt = writtenAt;
        this.price = price;
    }

    public StockHistory(Long id, LocalDateTime writtenAt, Long price) {
        this(writtenAt, price);
        this.id = id;
    }

    public LocalDateTime when() {
        return writtenAt;
    }

    public Long howMuch() {
        return price;
    }

    public Long id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockHistory that = (StockHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
