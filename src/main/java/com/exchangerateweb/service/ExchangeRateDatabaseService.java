package com.exchangerateweb.service;

import com.exchangerateweb.client.dto.ExchangeRatesJsonDTO;
import com.exchangerateweb.repository.dto.ExchangeRatesDatabaseDTO;
import com.exchangerateweb.repository.ExchangeRatesRepository;
import com.exchangerateweb.router.DataSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExchangeRateDatabaseService implements ExchangeRateService {
    private final ExchangeRatesRepository repository;

    public ExchangeRateDatabaseService(ExchangeRatesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Double getExchangeRate(String currency, LocalDate date) {
        ExchangeRatesDatabaseDTO dto = repository.findByCharCodeAndDate(currency, date)
                .orElseThrow(() -> new RuntimeException("No such currency found."));
        return dto.getValue() / dto.getNominal();
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.DATABASE;
    }
}
