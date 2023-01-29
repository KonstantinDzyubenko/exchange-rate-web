package com.exchangerateweb.service;

import com.exchangerateweb.client.ExchangeRatesJsonClient;
import com.exchangerateweb.client.dto.ExchangeRatesJsonDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateJsonServiceTest {
    @Mock
    private ExchangeRatesJsonClient client;
    @InjectMocks
    private ExchangeRateJsonService underTest;

    @Test
    public void happyPathTest() {
        when(client.getExchangeRatesByDate(any())).thenReturn(getExchangeRatesDTOMock());
        assertEquals(100d, underTest.getExchangeRate("USD", LocalDate.parse("2000-01-01")));
        verify(client).getExchangeRatesByDate(eq(LocalDate.parse("2000-01-01")));
    }

    @Test
    public void noCurrencyTest() {
        when(client.getExchangeRatesByDate(any())).thenReturn(getExchangeRatesDTOMock());
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> underTest.getExchangeRate("RUR", LocalDate.now()));
        assertEquals("No such currency found.", e.getMessage());
    }

    private ExchangeRatesJsonDTO getExchangeRatesDTOMock() {
        ExchangeRatesJsonDTO dto = new ExchangeRatesJsonDTO();
        ExchangeRatesJsonDTO.Currency currency = new ExchangeRatesJsonDTO.Currency();
        currency.setCharCode("USD");
        currency.setNominal(1);
        currency.setValue(100d);
        dto.setCurrencies(Map.of("USD", currency));
        return dto;
    }
}
