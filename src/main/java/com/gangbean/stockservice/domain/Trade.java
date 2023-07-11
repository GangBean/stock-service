package com.gangbean.stockservice.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private TradeType type;
    @ManyToOne
    private Account account;

    public Trade() {
    }

    public Trade(Account account, TradeType type, LocalDateTime dateTime, Long amount) {
        this.account = account;
        this.type = type;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public Trade(Long id, Account account, TradeType type, LocalDateTime dateTime, Long amount) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public LocalDateTime when() {
        return dateTime;
    }

    public Long howMuch() {
        return amount;
    }

    public TradeType how() {
        return type;
    }

    public Account fromWhat() {
        return account;
    }

    public Long id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(id, trade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
