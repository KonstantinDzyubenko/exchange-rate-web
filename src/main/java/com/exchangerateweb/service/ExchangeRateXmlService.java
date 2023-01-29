package com.exchangerateweb.service;

import com.exchangerateweb.client.ExchangeRatesXmlClient;
import com.exchangerateweb.client.dto.ExchangeRatesXmlDTO;
import com.exchangerateweb.router.DataSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExchangeRateXmlService implements ExchangeRateService {
    private final ExchangeRatesXmlClient client;

    public ExchangeRateXmlService(ExchangeRatesXmlClient client) {
        this.client = client;
    }

    @Override
    public Double getExchangeRate(String currency, LocalDate date) {
        ExchangeRatesXmlDTO dto = client.getExchangeRatesByDate(date);
        ExchangeRatesXmlDTO.Currency currency1 = dto.getCurrencies().stream()
                .filter(x -> x.getCharCode().equals(currency))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No such currency found."));
        double value = Double.parseDouble(currency1.getValue().replace(",", "."));
        return value / currency1.getNominal();
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.XML;
    }
}
