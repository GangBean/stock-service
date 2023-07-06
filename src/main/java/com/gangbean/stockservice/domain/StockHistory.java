package com.gangbean.stockservice.domain;

import java.time.LocalDateTime;

public class StockHistory {

    private Stock stock;
    private LocalDateTime writtenAt;
    private Long price;

    public StockHistory(Stock stock, LocalDateTime writtenAt, Long price) {
        this.stock = stock;
        this.writtenAt = writtenAt;
        this.price = price;
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
}
