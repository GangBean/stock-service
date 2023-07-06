package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findAllByAccountId(Long accountId);
}
