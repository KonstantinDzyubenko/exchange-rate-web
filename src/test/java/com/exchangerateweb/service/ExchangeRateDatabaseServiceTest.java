package com.exchangerateweb.service;

import com.exchangerateweb.repository.ExchangeRatesRepository;
import com.exchangerateweb.repository.dto.ExchangeRatesDatabaseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateDatabaseServiceTest {
    @Mock
    private ExchangeRatesRepository repository;
    @InjectMocks
    private ExchangeRateDatabaseService underTest;

    @Test
    public void happyPathTest() {
        when(repository.findByCharCodeAndDate(any(), any())).thenReturn(Optional.of(getExchangeRatesDTOMock()));
        assertEquals(100d, underTest.getExchangeRate("USD", LocalDate.parse("2022-01-10")));
        verify(repository).findByCharCodeAndDate(eq("USD"), eq(LocalDate.parse("2022-01-10")));
    }

    @Test
    public void noCurrencyTest() {
        when(repository.findByCharCodeAndDate(any(), any())).thenReturn(Optional.empty());
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> underTest.getExchangeRate("RUR", LocalDate.now()));
        assertEquals("No such currency found.", e.getMessage());
    }

    private ExchangeRatesDatabaseDTO getExchangeRatesDTOMock() {
        ExchangeRatesDatabaseDTO dto = new ExchangeRatesDatabaseDTO();
        dto.setDate(LocalDate.parse("2022-01-10"));
        dto.setValue(100d);
        dto.setName("US dollar");
        dto.setNominal(1);
        dto.setCharCode("USD");
        dto.setCurrencyId("R01235");
        return dto;
    }
}