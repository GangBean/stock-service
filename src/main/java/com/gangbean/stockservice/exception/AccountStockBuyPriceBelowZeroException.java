package com.gangbean.stockservice.exception;

import com.gangbean.stockservice.exception.account.AccountServiceException;

public class AccountStockBuyPriceBelowZeroException extends AccountServiceException {
    public AccountStockBuyPriceBelowZeroException(String message) {
        super(message);
    }
}
