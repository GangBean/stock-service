package com.gangbean.stockservice.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class StockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Stock stock;
    private LocalDateTime writtenAt;
    private Long price;

    public StockHistory() {

    }

    public StockHistory(Stock stock, LocalDateTime writtenAt, Long price) {
        this.stock = stock;
        this.writtenAt = writtenAt;
        this.price = price;
    }

    public StockHistory(Long id, Stock stock, LocalDateTime writtenAt, Long price) {
        this(stock, writtenAt, price);
        this.id = id;
    }

    public Stock ofWho() {
        return stock;
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
