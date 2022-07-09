package com.camargo.bullshorn.controller;

import com.camargo.bullshorn.model.CandleDataModel;
import com.camargo.bullshorn.model.SymbolInfoModel;
import com.camargo.bullshorn.service.CandleDataService;
import com.camargo.bullshorn.service.SymbolListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class DataController {

    private CandleDataService candleDataService;
    private SymbolListService symbolListService;

    public DataController(CandleDataService candleDataService, SymbolListService symbolListService) {
        this.candleDataService = candleDataService;
        this.symbolListService = symbolListService;
    }

    @GetMapping("/{market}")
    private List<SymbolInfoModel> getSymbolList(@PathVariable("market") String market){
        String exchange = market.equals("crypto") ? "Binance" : "US";
        List<SymbolInfoModel> response = symbolListService.getSymbolList(market, exchange);
        return response;
    }

    @GetMapping("/{market}/{resolution}/{symbol}")
    private List<CandleDataModel> getData(@PathVariable("market") String market, @PathVariable("resolution") String resolution,
                                               @PathVariable("symbol") String symbol){
        List<CandleDataModel> response = candleDataService.getCandleData(market, symbol, resolution);
        return response;
    }
}
