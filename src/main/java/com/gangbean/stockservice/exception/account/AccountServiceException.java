package com.gangbean.stockservice.exception.account;

import com.gangbean.stockservice.exception.StockServiceApplicationException;

public class AccountServiceException extends StockServiceApplicationException {
    public AccountServiceException(String message) {
        super(message);
    }
}
