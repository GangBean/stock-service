package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.AccountStockTrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountStockRepository extends JpaRepository<AccountStockTrade, Long> {
    List<AccountStockTrade> findAllByAccountIdAndStockId(Long accountId, Long stockId);
}
