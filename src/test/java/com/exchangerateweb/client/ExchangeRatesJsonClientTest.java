package com.exchangerateweb.client;

import com.exchangerateweb.client.dto.ExchangeRatesJsonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ExchangeRatesJsonClient.class)
public class ExchangeRatesJsonClientTest {
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private ExchangeRatesJsonClient underTest;

    @Test
    public void happyPathTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = Files.readString(Path.of("src/test/resources/__files/json/positive_response.json"), StandardCharsets.UTF_8);
        ExchangeRatesJsonDTO dto = mapper.readValue(json, ExchangeRatesJsonDTO.class);

        when(restTemplate.getForObject(anyString(), any())).thenReturn(dto);

        ExchangeRatesJsonDTO answer = new ExchangeRatesJsonDTO();
        answer.setDate(ZonedDateTime.parse("2023-01-10T08:30:00Z[UTC]"));
        answer.setPreviousDate(ZonedDateTime.parse("2022-12-31T08:30:00Z[UTC]"));
        answer.setPreviousURL("www.cbr-xml-daily.ru/archive/2023/01/10/daily_json.js");
        answer.setTimestamp(ZonedDateTime.parse("2023-01-10T12:00:00Z[UTC]"));

        {
            ExchangeRatesJsonDTO.Currency currency = new ExchangeRatesJsonDTO.Currency();
            currency.setId("R01010");
            currency.setNumCode("036");
            currency.setCharCode("AUD");
            currency.setNominal(1);
            currency.setName("РђРІСЃС‚СЂР°Р»РёР№СЃРєРёР№ РґРѕР»Р»Р°СЂ");
            currency.setValue(48.711d);
            currency.setPrevious(47.6537d);
            answer.getCurrencies().put("AUD", currency);
        }

        {
            ExchangeRatesJsonDTO.Currency currency = new ExchangeRatesJsonDTO.Currency();
            currency.setId("R01235");
            currency.setNumCode("840");
            currency.setCharCode("USD");
            currency.setNominal(1);
            currency.setName("Р”РѕР»Р»Р°СЂ РЎРЁРђ");
            currency.setValue(70.3002d);
            currency.setPrevious(70.3375d);
            answer.getCurrencies().put("USD", currency);
        }

        Assertions.assertEquals(answer, underTest.getExchangeRatesByDate(LocalDate.parse("2022-12-31")));
        verify(restTemplate).getForObject(eq("https://www.cbr-xml-daily.ru/archive/2022/12/31/daily_json.js"),
                eq(ExchangeRatesJsonDTO.class));
    }
}
