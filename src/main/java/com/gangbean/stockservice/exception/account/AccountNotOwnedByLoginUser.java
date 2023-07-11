package com.gangbean.stockservice.exception.account;

public class AccountNotOwnedByLoginUser extends AccountServiceException {
    public AccountNotOwnedByLoginUser(String message) {
        super(message);
    }
}
