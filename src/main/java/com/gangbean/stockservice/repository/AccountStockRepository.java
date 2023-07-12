package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.AccountStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountStockRepository extends JpaRepository<AccountStock, Long> {
    Optional<AccountStock> findByAccountIdAndStockId(Long accountId, Long stockId);
}
