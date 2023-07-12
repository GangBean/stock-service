package com.gangbean.stockservice.exception;

import com.gangbean.stockservice.exception.account.AccountServiceException;

public class AccountStockSellAmountBelowZeroException extends AccountServiceException {
    public AccountStockSellAmountBelowZeroException(String message) {
        super(message);
    }
}
