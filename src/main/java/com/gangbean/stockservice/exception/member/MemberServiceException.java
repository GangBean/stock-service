package com.gangbean.stockservice.exception.member;

import com.gangbean.stockservice.exception.StockServiceApplicationException;

public class MemberServiceException extends StockServiceApplicationException {
    public MemberServiceException(String message) {
        super(message);
    }
}
