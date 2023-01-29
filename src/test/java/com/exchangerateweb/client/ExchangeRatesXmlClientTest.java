package com.exchangerateweb.client;

import com.exchangerateweb.client.dto.ExchangeRatesXmlDTO;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ExchangeRatesXmlClient.class)
public class ExchangeRatesXmlClientTest {
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private ExchangeRatesXmlClient underTest;

    @Test
    public void happyPathTest() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        String xml = Files.readString(Path.of("src/test/resources/__files/xml/positive_response.xml"), StandardCharsets.UTF_8);
        ExchangeRatesXmlDTO dto = xmlMapper.readValue(xml, ExchangeRatesXmlDTO.class);

        when(restTemplate.getForObject(anyString(), any())).thenReturn(dto);

        ExchangeRatesXmlDTO answer = new ExchangeRatesXmlDTO();
        answer.setDate(LocalDate.parse("2022-12-31"));
        answer.setName("Foreign Currency Market");

        {
            ExchangeRatesXmlDTO.Currency currency = new ExchangeRatesXmlDTO.Currency();
            currency.setId("R01010");
            currency.setNumCode("036");
            currency.setCharCode("AUD");
            currency.setNominal(1);
            currency.setName("Австралийский доллар");
            currency.setValue("47,6537");
            answer.getCurrencies().add(currency);
        }

        {
            ExchangeRatesXmlDTO.Currency currency = new ExchangeRatesXmlDTO.Currency();
            currency.setId("R01020A");
            currency.setNumCode("944");
            currency.setCharCode("AZN");
            currency.setNominal(1);
            currency.setName("Азербайджанский манат");
            currency.setValue("41,3750");
            answer.getCurrencies().add(currency);
        }

        Assertions.assertEquals(answer, underTest.getExchangeRatesByDate(LocalDate.parse("2022-12-31")));
        verify(restTemplate).getForObject(eq("https://cbr.ru/scripts/XML_daily.asp?date_req=31/12/2022"),
                eq(ExchangeRatesXmlDTO.class));
    }
}
