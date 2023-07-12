package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.Stock;
import com.gangbean.stockservice.dto.StockDetailInfoResponse;
import com.gangbean.stockservice.dto.StockInfoResponse;
import com.gangbean.stockservice.exception.StockNotFoundException;
import com.gangbean.stockservice.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockInfoResponse> respondsOfAllStock() {
        return stockRepository.findAll().stream()
                .map(StockInfoResponse::responseOf)
                .collect(Collectors.toList());
    }

    public StockDetailInfoResponse responseOfStockDetail(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException("입력된 ID에 해당하는 주식이 존재하지 않습니다: " + stockId));
        return StockDetailInfoResponse.responseOf(stock);
    }
}
