package com.example.demo.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demoe.ExternalException.ExternalAPIException;

@Service
public class CurrencyService {
        //api with url and api key
	private final String API_URL="https://openexchangerates.org/api/latest.json?app_id=43412cd612294c08bc79e576fc432dad";

	private final RestTemplate restTemplate;

	public CurrencyService(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
	}
           
	public Map<String, Double> getExchangeRates(String baseCurrency) {
		try {
			String url=API_URL+"&base="+baseCurrency;
			ResponseEntity<Map> response=restTemplate.exchange(url,HttpMethod.GET,null,Map.class);

			// Get the rates from response body
			Map<String,Object> rates=(Map<String, Object>)response.getBody().get("rates");

			Map<String,Double> exchangeRates=new HashMap<>();
			for (Map.Entry<String, Object> entry : rates.entrySet()) {
				Object rate=entry.getValue();
				if (rate instanceof Integer) {
					exchangeRates.put(entry.getKey(), ((Integer) rate).doubleValue());
				} else if (rate instanceof Double) {
					exchangeRates.put(entry.getKey(), (Double) rate);
				}
			}

			return exchangeRates;

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			//error
			System.out.println("fetching error...: " + e.getMessage());
			throw new ExternalAPIException("Failed to fetch rates...........");
		} catch (Exception e) {
			//Catch any other unexpected errors
			System.out.println("an unexpected error occurred...: " + e.getMessage());
			throw new RuntimeException("an unexpected error occurred while fetching rate");
		}
	}

	// to  Handle invalid currency 
	public double convertAmount(String fromCurrency,String toCurrency,double amount) {
		Map<String,Double> rates=getExchangeRates(fromCurrency);

		if (!rates.containsKey(toCurrency)) {
			throw new IllegalArgumentException("Invalid target currency code: " + toCurrency);
		}

		double rate = rates.get(toCurrency);
		return amount * rate;
	}
}
