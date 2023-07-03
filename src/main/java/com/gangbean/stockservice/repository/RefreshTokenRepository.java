package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteAllByMemberId(Long memberId);
}
