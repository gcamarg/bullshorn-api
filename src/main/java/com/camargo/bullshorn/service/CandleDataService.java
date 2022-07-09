package com.camargo.bullshorn.service;

import com.camargo.bullshorn.model.FinnhubCandleDataModel;
import com.camargo.bullshorn.model.CandleDataModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandleDataService {

    @Value("${finnhub.token}")
    private String token;
    public List<CandleDataModel> getCandleData(String market, String symbol, String resolution){

        if(market.equals("crypto")) symbol = "BINANCE:" + symbol;
        long unixTimeNow = System.currentTimeMillis() / 1000L;
        String uri = "https://finnhub.io/api/v1/"+market+"/candle?token="+token+"&symbol="
                +symbol+"&resolution="+resolution+"&from=1574155390&to="+Long.toString(unixTimeNow);
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
}
