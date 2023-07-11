package com.gangbean.stockservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountTransferRequest {

    private String receiverAccountNumber;

    private Long amount;
}
