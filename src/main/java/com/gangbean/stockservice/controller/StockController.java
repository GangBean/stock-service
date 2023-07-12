package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.dto.ExceptionResponse;
import com.gangbean.stockservice.dto.StockDetailInfoResponse;
import com.gangbean.stockservice.exception.StockNotFoundException;
import com.gangbean.stockservice.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stocks/{stockId}")
    public ResponseEntity<StockDetailInfoResponse> stockDetail(@PathVariable Long stockId) {
        return ResponseEntity.ok(stockService.responseOfStockDetail(stockId));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleError(StockNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
