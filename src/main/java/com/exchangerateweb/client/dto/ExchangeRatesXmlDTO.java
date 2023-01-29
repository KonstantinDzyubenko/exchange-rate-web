package com.exchangerateweb.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "ValCurs")
public class ExchangeRatesXmlDTO {
    @JacksonXmlProperty(isAttribute = true, localName = "Date")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;
    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private String name;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Valute")
    private List<Currency> currencies = new ArrayList<>();

    @Data
    public static class Currency {
        @JacksonXmlProperty(isAttribute = true, localName = "ID")
        private String id;
        @JacksonXmlProperty(localName = "NumCode")
        private String numCode;
        @JacksonXmlProperty(localName = "CharCode")
        private String charCode;
        @JacksonXmlProperty(localName = "Nominal")
        private int nominal;
        @JacksonXmlProperty(localName = "Name")
        private String name;
        @JacksonXmlProperty(localName = "Value")
        private String value;
    }
}