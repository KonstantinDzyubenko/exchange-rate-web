package com.exchangerateweb.controller;

import com.exchangerateweb.repository.ExchangeRatesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {
    private final ExchangeRatesRepository exchangeRatesRepository;

    public DebugController(ExchangeRatesRepository exchangeRatesRepository) {
        this.exchangeRatesRepository = exchangeRatesRepository;
    }

    @GetMapping("/debug")
    public void debug() {
        System.out.println();
    }
}
