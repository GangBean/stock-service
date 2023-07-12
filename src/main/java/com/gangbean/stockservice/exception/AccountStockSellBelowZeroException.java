package com.gangbean.stockservice.exception;

import com.gangbean.stockservice.exception.account.AccountServiceException;

public class AccountStockSellBelowZeroException extends AccountServiceException {
    public AccountStockSellBelowZeroException(String message) {
        super(message);
    }
}
