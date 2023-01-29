package com.exchangerateweb.controller;

import com.exchangerateweb.router.DataSource;
import com.exchangerateweb.router.ExchangeRateRouter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRateRouter router;

    @Test
    public void happyPathTest() throws Exception {
        when(router.getExchangeRate(any(), any(), any())).thenReturn(300d);
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "USD")
                        .param("date", "2022-01-30")
                        .param("dataSource", "xml"))
                .andExpect(content().string("300.0"));
        verify(router).getExchangeRate(eq("USD"), eq(LocalDate.parse("2022-01-30")), eq(DataSource.XML));
    }

    @Test
    public void defaultDataSourceTest() throws Exception {
        when(router.getExchangeRate(any(), any(), any())).thenReturn(300d);
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "USD")
                        .param("date", "2022-01-30"))
                .andExpect(content().string("300.0"));
        verify(router).getExchangeRate(eq("USD"), eq(LocalDate.parse("2022-01-30")), eq(DataSource.JSON));
    }

    @Test
    public void nullParametersTest() throws Exception {
        mockMvc.perform(get("/exchangerate"))
                .andExpect(content().string(containsString("getExchangeRate.date: must not be null")))
                .andExpect(content().string(containsString("getExchangeRate.currency: must not be null")))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void invalidDateTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "USD")
                        .param("date", "20220130"))
                .andExpect(content().string(containsString("Failed to convert value")))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void futureDateTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "USD")
                        .param("date", LocalDate.now().plusDays(1).toString()))
                .andExpect(content().string("getExchangeRate.date: must be a date in the past or in the present"))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void invalidDataSourceTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "USD")
                        .param("date", "2022-01-30")
                        .param("dataSource", "invalid"))
                .andExpect(content().string(containsString("Failed to convert value")))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}
