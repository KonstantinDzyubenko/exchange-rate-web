package com.exchangerateweb.repository.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_exchange_rates")
public class ExchangeRatesDatabaseDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "currency_id")
    private String currencyId;
    @Column(name = "num_code")
    private String numCode;
    @Column(name = "char_code")
    private String charCode;
    @Column(name = "nominal")
    private int nominal;
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private double value;
    @Column(name = "date")
    private LocalDate date;
}
