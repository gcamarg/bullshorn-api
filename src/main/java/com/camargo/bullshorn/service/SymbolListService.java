package com.camargo.bullshorn.service;

import com.camargo.bullshorn.model.SymbolInfoModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SymbolListService {

    @Value("${finnhub.token}")
    private String token;
    public List<SymbolInfoModel> getSymbolList(String market, String exchange){

        String uri = "https://finnhub.io/api/v1/"+market+"/symbol?exchange="+exchange+"&token="+token;
        RestTemplate restTemplate = new RestTemplate();
        List<SymbolInfoModel> symbolList = new ArrayList<SymbolInfoModel> (
                Arrays.asList(
                        restTemplate.getForObject(uri, SymbolInfoModel[].class)
                ));
        List<String> symbolsToFilter = new ArrayList<>();
        if(market.equals("stock")) symbolsToFilter.addAll(
                Arrays.asList("TSLA", "AAPL", "AMZN", "EVFM", "ENDP", "AMD", "META", "NVDA"));
        else if(market.equals("crypto")) symbolsToFilter.addAll(
                Arrays.asList("ETH/BTC", "ETH/USDT", "BTC/USDT", "BNB/USDT", "BTC/BNB", "ADA/USDT", "DOGE/USDT", "XRP/USDT"));
        symbolList.removeIf(s -> !symbolsToFilter.contains(s.getDisplaySymbol()));
        symbolList.sort((SymbolInfoModel s1, SymbolInfoModel s2) -> s1.getDisplaySymbol().compareTo(s2.getDisplaySymbol()));
        return symbolList;

    }

}
