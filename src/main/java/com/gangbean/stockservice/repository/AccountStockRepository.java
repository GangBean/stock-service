package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.AccountStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountStockRepository extends JpaRepository<AccountStock, Long> {
    List<AccountStock> findAllByAccountIdAndStockId(Long accountId, Long stockId);
}
