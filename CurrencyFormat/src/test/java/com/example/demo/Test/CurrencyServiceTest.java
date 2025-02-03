package com.example.demo.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Service.CurrencyService;
import com.example.demoe.ExternalException.ExternalAPIException;

public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {  //initialize mock object before test
         
    }

    @Test
    public void testConvertCurrencyValid() {
        //mock response for the API
        Map<String, Double> mockRates = Map.of("USD",1.0,"EUR",0.85);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),any(),eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("rates", mockRates)));

        //conversion
        Double result = currencyService.convertAmount("USD","EUR",100.0);
        assertEquals(85.0,result);
    }
    
    @Test
    public void testConvertCurrencyInvalidCode() {
        // mock response for the API
        Map<String, Double> mockRates = Map.of("USD", 1.0, "EUR", 0.85);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.GET), any(),eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("rates",mockRates)));

        //  invalid target currency code
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyService.convertAmount("USD","GBP", 100.0);
        });

        assertEquals("Invalid target currency", exception.getMessage());
    }
    
    @Test
    public void testGetExchangeRatesAPIUnavailable() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Map.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Exception exception = assertThrows(ExternalAPIException.class, () -> {
            currencyService.getExchangeRates("USD");
        });

        assertEquals("Failed to fetch rates", exception.getMessage());
    }
}
