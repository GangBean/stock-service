package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumber(String number);

    List<Account> findAllByMemberId(Long memberId);

    @Query("SELECT a, m, s FROM Account a LEFT OUTER JOIN a.member m LEFT OUTER JOIN FETCH a.stocks s WHERE a.id = :id")
    Optional<Account> findOneWithMemberAndStocksById(@Param("id") Long id);

}
