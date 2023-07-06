package com.gangbean.stockservice.service;

import com.gangbean.stockservice.dto.StockInfoResponse;
import com.gangbean.stockservice.repository.StockRepository;

import java.util.List;
import java.util.stream.Collectors;

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
}
