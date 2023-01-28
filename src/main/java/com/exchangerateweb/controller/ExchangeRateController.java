package com.exchangerateweb.controller;

import com.exchangerateweb.router.DataSource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Validated
@RestController
public class ExchangeRateController {

    @GetMapping("/exchangerate")
    public Double getExchangeRate(@RequestParam(required = false) @NotNull String currency,
                                  @RequestParam(required = false) @PastOrPresent @NotNull LocalDate date,
                                  @RequestParam(required = false, defaultValue = "json") DataSource dataSource) {
        return 300d;
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        return e.getMessage();
    }
}
