package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.Stock;
import com.gangbean.stockservice.dto.StockBuyRequest;
import com.gangbean.stockservice.dto.StockBuyResponse;
import com.gangbean.stockservice.dto.StockDetailInfoResponse;
import com.gangbean.stockservice.dto.StockInfoResponse;
import com.gangbean.stockservice.exception.StockNotFoundException;
import com.gangbean.stockservice.repository.StockHistoryRepository;
import com.gangbean.stockservice.repository.StockRepository;

import java.util.List;
import java.util.stream.Collectors;

public class StockService {
    private final StockRepository stockRepository;

    private final StockHistoryRepository stockHistoryRepository;

    public StockService(StockRepository stockRepository, StockHistoryRepository stockHistoryRepository) {
        this.stockRepository = stockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public List<StockInfoResponse> respondsOfAllStock() {
        return stockRepository.findAll().stream()
                .map(StockInfoResponse::responseOf)
                .collect(Collectors.toList());
    }

    public StockDetailInfoResponse responseOfStockDetail(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException("입력된 ID에 해당하는 주식이 존재하지 않습니다: " + stockId));
        return StockDetailInfoResponse.responseOf(stock, stockHistoryRepository.findAllByStockId(stock.id()));
    }

    public StockBuyResponse responseOfBuy(StockBuyRequest stockBuyRequest) {
        return StockBuyResponse.responseOf();
    }
}
