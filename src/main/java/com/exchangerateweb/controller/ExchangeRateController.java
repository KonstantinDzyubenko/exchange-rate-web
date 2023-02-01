package com.exchangerateweb.controller;

import com.exchangerateweb.router.DataSource;
import com.exchangerateweb.router.ExchangeRateRouter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Validated
@RestController
@Slf4j
public class ExchangeRateController {
    private final ExchangeRateRouter router;

    public ExchangeRateController(ExchangeRateRouter router) {
        this.router = router;
    }

    @GetMapping("/exchangerate")
    public Double getExchangeRate(@RequestParam(required = false) @NotNull String currency,
                                  @RequestParam(required = false) @PastOrPresent @NotNull LocalDate date,
                                  @RequestParam(required = false, defaultValue = "json") DataSource dataSource) {
        return router.getExchangeRate(currency, date, dataSource);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(Exception e) {
        return e.getMessage();
    }
}
