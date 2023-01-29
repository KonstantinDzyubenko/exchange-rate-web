package com.exchangerateweb.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class ExchangeRatesJsonDTO {
    @JsonProperty("Date")
    private ZonedDateTime date;
    @JsonProperty("PreviousDate")
    private ZonedDateTime previousDate;
    @JsonProperty("PreviousURL")
    private String previousURL;
    @JsonProperty("Timestamp")
    private ZonedDateTime timestamp;
    @JsonProperty("Valute")
    private Map<String, Currency> currencies = new HashMap<>();

    @Data
    public static class Currency {
        @JsonProperty("ID")
        private String id;
        @JsonProperty("NumCode")
        private String numCode;
        @JsonProperty("CharCode")
        private String charCode;
        @JsonProperty("Nominal")
        private int nominal;
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Value")
        private double value;
        @JsonProperty("Previous")
        private double previous;
    }
}
