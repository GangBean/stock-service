package com.gangbean.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockBuyRequest {

    private BigDecimal amount;

    private BigDecimal price;
}
