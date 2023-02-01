package com.exchangerateweb.repository;

import com.exchangerateweb.repository.dto.ExchangeRatesDatabaseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExchangeRatesRepositoryTest {
    @Autowired
    private ExchangeRatesRepository underTest;

    @Test
    @Sql(value = "/__files/database/data.sql", config = @SqlConfig(encoding = "utf-8"))
    public void happyPathTest() {
        ExchangeRatesDatabaseDTO dto = underTest
                .findByCharCodeAndDate("AUD", LocalDate.parse("2022-01-10")).get();
        assertEquals("AUD", dto.getCharCode());
        assertEquals("R01010", dto.getCurrencyId());
        assertEquals("Австралийский доллар", dto.getName());
        assertEquals(1, dto.getNominal());
        assertEquals("036", dto.getNumCode());
        assertEquals(47.6537d, dto.getValue());
        assertEquals(LocalDate.parse("2022-01-10"), dto.getDate());
    }

    @Test
    @Sql(value = "/__files/database/data.sql", config = @SqlConfig(encoding = "utf-8"))
    public void invalidDateTest() {
        Optional<ExchangeRatesDatabaseDTO> dto = underTest
                .findByCharCodeAndDate("AUD", LocalDate.parse("2023-01-10"));
        assertTrue(dto.isEmpty());
    }

    @Test
    @Sql(value = "/__files/database/data.sql", config = @SqlConfig(encoding = "utf-8"))
    public void invalidCharCodeTest() {
        Optional<ExchangeRatesDatabaseDTO> dto = underTest
                .findByCharCodeAndDate("RUR", LocalDate.parse("2022-01-10"));
        assertTrue(dto.isEmpty());
    }
}