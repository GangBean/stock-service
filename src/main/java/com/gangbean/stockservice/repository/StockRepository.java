package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
