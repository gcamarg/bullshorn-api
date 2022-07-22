package com.camargo.bullshorn.marketdata.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuoteDataModel {

    private BigDecimal c;
    private BigDecimal d;
    private BigDecimal dp;
    private BigDecimal h;
    private BigDecimal l;
    private BigDecimal o;
    private BigDecimal pc;
    private Long t;
}
