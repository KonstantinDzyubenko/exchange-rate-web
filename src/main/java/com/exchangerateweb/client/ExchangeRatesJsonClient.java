package com.exchangerateweb.client;

import com.exchangerateweb.client.dto.ExchangeRatesJsonDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ExchangeRatesJsonClient {
    private final String urlTemplate;
    private final DateTimeFormatter formatter;
    private final RestTemplate restTemplate;

    public ExchangeRatesJsonClient(@Value("${client.json.url-template}") String urlTemplate,
                                   @Value("${client.json.date-template}") String dateTemplate,
                                   RestTemplate restTemplate) {
        this.urlTemplate = urlTemplate;
        this.formatter = DateTimeFormatter.ofPattern(dateTemplate);
        this.restTemplate = restTemplate;
    }

    public ExchangeRatesJsonDTO getExchangeRatesByDate(LocalDate date) {
        String url = makeURLByDate(date);
        try {
            return restTemplate.getForObject(url, ExchangeRatesJsonDTO.class);
        } catch (Exception e) {
            log.error("Failed to retrieve data for " + url, e);
            throw new RuntimeException("Unable to get exchange rate data from the source.");
        }
    }

    private String makeURLByDate(LocalDate date) {
        return String.format(urlTemplate, date.format(formatter));
    }
}
