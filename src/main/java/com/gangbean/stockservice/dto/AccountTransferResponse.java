package com.gangbean.stockservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountTransferResponse {

    private Long id;

    private Long balance;

    public AccountTransferResponse(Long balance) {
        this.balance = balance;
    }

    public static AccountTransferResponse responseOf(Long balance) {
        return new AccountTransferResponse(balance);
    }

    public Long balance() {
        return balance;
    }
}
