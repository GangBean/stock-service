package com.gangbean.stockservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class AccountTransferResponse {

    private Long id;

    private BigDecimal balance;

    public AccountTransferResponse(BigDecimal balance) {
        this.balance = balance;
    }

    public static AccountTransferResponse responseOf(BigDecimal balance) {
        return new AccountTransferResponse(balance);
    }

    public BigDecimal balance() {
        return balance;
    }
}
