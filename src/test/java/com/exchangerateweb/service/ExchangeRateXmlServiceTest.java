package com.exchangerateweb.service;

import com.exchangerateweb.client.ExchangeRatesXmlClient;
import com.exchangerateweb.client.dto.ExchangeRatesXmlDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateXmlServiceTest {
    @Mock
    private ExchangeRatesXmlClient client;
    @InjectMocks
    private ExchangeRateXmlService underTest;

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

    private ExchangeRatesXmlDTO getExchangeRatesDTOMock() {
        ExchangeRatesXmlDTO dto = new ExchangeRatesXmlDTO();
        ExchangeRatesXmlDTO.Currency currency = new ExchangeRatesXmlDTO.Currency();
        currency.setCharCode("USD");
        currency.setNominal(1);
        currency.setValue("100");
        dto.setCurrencies(List.of(currency));
        return dto;
    }
}
