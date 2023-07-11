package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumber(String number);

    List<Account> findAllByMemberUserId(Long memberId);
}
