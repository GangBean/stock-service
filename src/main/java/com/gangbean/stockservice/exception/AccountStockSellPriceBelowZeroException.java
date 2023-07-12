package com.gangbean.stockservice.exception;

import com.gangbean.stockservice.exception.account.AccountServiceException;

public class AccountStockSellPriceBelowZeroException extends AccountServiceException {
    public AccountStockSellPriceBelowZeroException(String message) {
        super(message);
    }
}
