package com.gangbean.stockservice.repository;

import com.gangbean.stockservice.domain.Stock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findAllByOrderByNameDesc();

    Optional<Stock> findTop10ByIdOrderByHistoriesWrittenAtDesc(Long id);

    @Query("SELECT s FROM Stock s LEFT OUTER JOIN FETCH s.histories h WHERE s.id = :id AND h.writtenAt > :prevWrittenAt ORDER BY h.writtenAt DESC")
    Optional<Stock> findTop10ByIdAndHistoriesWrittenAtGreaterThanOrderByWrittenAtDesc(@Param("id") Long id
        , @Param("prevWrittenAt") LocalDateTime prevLastEntityIndex);

    List<Stock> findTop10ByOrderById();
}
