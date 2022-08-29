package com.camargo.bullshorn.marketdata;

import com.camargo.bullshorn.marketdata.model.CandleDataModel;
import com.camargo.bullshorn.marketdata.model.SymbolInfoModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${client_domain}")
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

    @GetMapping("/{market}/{resolution}/{symbol}")
    private List<CandleDataModel> getData(@PathVariable("market") String market, @PathVariable("resolution") String resolution,
                                          @PathVariable("symbol") String symbol) {
        List<CandleDataModel> response = marketDataService.getCandleData(market, symbol, resolution);
        return response;
    }
}
