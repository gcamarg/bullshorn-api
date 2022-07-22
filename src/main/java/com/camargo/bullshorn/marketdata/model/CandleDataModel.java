package com.camargo.bullshorn.marketdata.model;

import lombok.Data;

@Data
public class CandleDataModel {

    private long date;
    private float open;
    private float high;
    private float low;
    private float close;

    public CandleDataModel(long date, float open, float high, float low, float close) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }
}
