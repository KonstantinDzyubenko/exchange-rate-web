package com.exchangerateweb.router;

import com.exchangerateweb.service.ExchangeRateService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExchangeRateRouter {
    private final Map<DataSource, ExchangeRateService> services;

    public ExchangeRateRouter(List<ExchangeRateService> services) {
        this.services = services.stream()
                .collect(Collectors.toMap(ExchangeRateService::getDataSource, Function.identity()));
    }

    public Double getExchangeRate(String currency, LocalDate date, DataSource dataSource) {
        if (!services.containsKey(dataSource)) {
            throw new RuntimeException("No such data source found.");
        }
        return services.get(dataSource).getExchangeRate(currency, date);
    }
}
