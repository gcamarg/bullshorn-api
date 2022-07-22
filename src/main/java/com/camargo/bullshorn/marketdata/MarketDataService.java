package com.camargo.bullshorn.marketdata;

import com.camargo.bullshorn.marketdata.model.FinnhubCandleDataModel;
import com.camargo.bullshorn.marketdata.model.CandleDataModel;
import com.camargo.bullshorn.marketdata.model.QuoteDataModel;
import com.camargo.bullshorn.marketdata.model.SymbolInfoModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MarketDataService {

    @Value("${finnhub.token}")
    private String token;
    private final String FINNHUB_URI = "https://finnhub.io/api/v1/";
    public List<CandleDataModel> getCandleData(String market, String symbol, String resolution) {

        if (market.equals("crypto")) symbol = "BINANCE:" + symbol;
        long unixTimeNow = System.currentTimeMillis() / 1000L;
        String uri = FINNHUB_URI + market + "/candle?token=" + token + "&symbol="
                + symbol + "&resolution=" + resolution + "&from=1574155390&to=" + Long.toString(unixTimeNow);
        RestTemplate restTemplate = new RestTemplate();
        FinnhubCandleDataModel data = restTemplate.getForObject(uri, FinnhubCandleDataModel.class);

        List<CandleDataModel> arrayCandleData = new ArrayList<CandleDataModel>();
        for (int i = 0; i < data.getT().size(); i++) {
            CandleDataModel candleData = new CandleDataModel(data.getT().get(i),
                    data.getO().get(i),
                    data.getH().get(i),
                    data.getL().get(i),
                    data.getC().get(i));
            arrayCandleData.add(candleData);
        }
        return arrayCandleData;
    }

    public QuoteDataModel getTodaysQuote(String symbol){
        String uri = FINNHUB_URI + "/quote?token=" + token + "&symbol=" + symbol ;
        RestTemplate restTemplate = new RestTemplate();
        QuoteDataModel quote = restTemplate.getForObject(uri, QuoteDataModel.class);

        return quote;
    }

    public List<SymbolInfoModel> getSymbolList(String market, String exchange) {

        String uri = "https://finnhub.io/api/v1/" + market + "/symbol?exchange=" + exchange + "&token=" + token;
        RestTemplate restTemplate = new RestTemplate();
        List<SymbolInfoModel> symbolList = new ArrayList<SymbolInfoModel>(
                Arrays.asList(
                        restTemplate.getForObject(uri, SymbolInfoModel[].class)
                ));
        List<String> symbolsToFilter = new ArrayList<>();
        if (market.equals("stock")) symbolsToFilter.addAll(
                Arrays.asList("TSLA", "AAPL", "AMZN", "EVFM", "ENDP", "AMD", "META", "NVDA"));
        else if (market.equals("crypto")) symbolsToFilter.addAll(
                Arrays.asList("ETH/BTC", "ETH/USDT", "BTC/USDT", "BNB/USDT", "BTC/BNB", "ADA/USDT", "DOGE/USDT", "XRP/USDT"));
        symbolList.removeIf(s -> !symbolsToFilter.contains(s.getDisplaySymbol()));
        symbolList.sort((SymbolInfoModel s1, SymbolInfoModel s2) -> s1.getDisplaySymbol().compareTo(s2.getDisplaySymbol()));
        return symbolList;

    }
}
