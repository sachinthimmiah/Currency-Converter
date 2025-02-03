package com.example.demo.Controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Entity.Currency;
import com.example.demo.Service.CurrencyService;

@RestController
@RequestMapping("/api")   //base url for all request
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService=currencyService;
    }

    @GetMapping("/rates")   // fetching the rates ,//defaultvalue=USD
    public Map<String,Double> getExchangeRates(@RequestParam(value="base",defaultValue="USD") String baseCurrency) {
        return currencyService.getExchangeRates(baseCurrency);
    }

    @PostMapping("/convert")   //Convert amount from format to another format
    public Map<String,Object> convertCurrency(@RequestBody Currency request) {
        double convertedAmount=currencyService.convertAmount(request.getFrom(),request.getTo(),request.getAmount());
        return Map.of(
            "from", request.getFrom(),
            "to", request.getTo(),
            "amount", request.getAmount(),
            "convertedAmount", convertedAmount
        );
    }
}

