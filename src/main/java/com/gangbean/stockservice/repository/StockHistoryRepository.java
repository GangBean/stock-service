package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    List<StockHistory> findAllByStockId(Long stockId);
}
