package com.camargo.bullshorn.marketdata;

import com.camargo.bullshorn.marketdata.model.CandleDataModel;
import com.camargo.bullshorn.marketdata.model.SymbolInfoModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/marketdata")
public class MarketDataController {

    private MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/{market}")
    private List<SymbolInfoModel> getSymbolList(@PathVariable("market") String market) {
        String exchange = market.equals("crypto") ? "Binance" : "US";
        List<SymbolInfoModel> response = marketDataService.getSymbolList(market, exchange);
        return response;
    }

    @GetMapping("/suggestion")
    private ResponseEntity<List<String>> getSymbolList() {

        List<String> responseList = marketDataService.getSymbolSugestion();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    @GetMapping("/{market}/{resolution}/{symbol}")
    private List<CandleDataModel> getData(@PathVariable("market") String market, @PathVariable("resolution") String resolution,
                                          @PathVariable("symbol") String symbol) {
        List<CandleDataModel> response = marketDataService.getCandleData(market, symbol, resolution);
        return response;
    }
}
