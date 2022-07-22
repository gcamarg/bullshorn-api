package com.camargo.bullshorn.marketdata.model;

import lombok.Data;

@Data
public class SymbolInfoModel {
    private String currency;
    private String description;
    private String displaySymbol;
    private String figi;
    private String isin;
    private String mic;
    private String shareClassFIGI;
    private String symbol;
    private String symbol2;
    private String type;
}
