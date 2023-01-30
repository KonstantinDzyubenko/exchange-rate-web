package com.exchangerateweb.repository;

import com.exchangerateweb.repository.dto.ExchangeRatesDatabaseDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ExchangeRatesRepository extends CrudRepository<ExchangeRatesDatabaseDTO, Integer> {
    Optional<ExchangeRatesDatabaseDTO> findByCharCodeAndDate(String charCode, LocalDate date);
}
