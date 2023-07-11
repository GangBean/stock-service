package com.gangbean.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockBuyRequest {

    private Long amount;

    private Long price;
}
