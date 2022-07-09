package com.camargo.bullshorn.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class FinnhubCandleDataModel {

    private ArrayList<Float> c;
    private ArrayList<Float> h;
    private ArrayList<Float> l;
    private ArrayList<Float> o;
    private String s;
    private ArrayList<Long> t;
    private ArrayList<Float> v;


}
