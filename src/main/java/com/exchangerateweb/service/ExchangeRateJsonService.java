package com.exchangerateweb.service;

import com.exchangerateweb.client.ExchangeRatesJsonClient;
import com.exchangerateweb.client.dto.ExchangeRatesJsonDTO;
import com.exchangerateweb.router.DataSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExchangeRateJsonService implements ExchangeRateService {
    private final ExchangeRatesJsonClient client;

    public ExchangeRateJsonService(ExchangeRatesJsonClient client) {
        this.client = client;
    }

    @Override
    public Double getExchangeRate(String currency, LocalDate date) {
        ExchangeRatesJsonDTO dto = client.getExchangeRatesByDate(date);
        if (!dto.getCurrencies().containsKey(currency)) {
            throw new RuntimeException("No such currency found.");
        }
        ExchangeRatesJsonDTO.Currency currency1 = dto.getCurrencies().get(currency);
        return currency1.getValue() / currency1.getNominal();
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.JSON;
    }
}
