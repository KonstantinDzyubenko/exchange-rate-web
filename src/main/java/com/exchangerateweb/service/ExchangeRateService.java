package com.exchangerateweb.service;

import com.exchangerateweb.router.DataSource;

import java.time.LocalDate;

public interface ExchangeRateService {
    Double getExchangeRate(String currency, LocalDate date);
    DataSource getDataSource();
}
