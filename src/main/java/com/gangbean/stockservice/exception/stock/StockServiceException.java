package com.gangbean.stockservice.exception.stock;

import com.gangbean.stockservice.exception.StockServiceApplicationException;

public class StockServiceException extends StockServiceApplicationException {
    public StockServiceException(String message) {
        super(message);
    }
}
