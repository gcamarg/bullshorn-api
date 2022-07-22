package com.camargo.bullshorn.userInvestments;

import com.camargo.bullshorn.marketdata.MarketDataService;
import com.camargo.bullshorn.marketdata.model.QuoteDataModel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/investments")
public class UserInvestmentsController {

    private UserInvestmentsService userInvestmentsService;
    private MarketDataService marketDataService;

    @PostMapping("/")
    public String addInvestment(@RequestBody UserInvestments userInvestments) {
        return userInvestmentsService.addInvestment(userInvestments);

    }
    @GetMapping("/")
    public List<UserInvestments> getInvestments(){
        return userInvestmentsService.getUserInvestments();
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteInvestmentItem(@RequestParam("id") Long id){
        boolean isDeleted = userInvestmentsService.deleteInvestmentItem(id);
        if(!isDeleted){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/quote")
    public ResponseEntity<QuoteDataModel> getQuote(@RequestParam("symbol") String symbol){

        if(symbol==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(marketDataService.getTodaysQuote(symbol), HttpStatus.OK);

    }
}
