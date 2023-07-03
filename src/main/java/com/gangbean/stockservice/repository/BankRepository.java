package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
