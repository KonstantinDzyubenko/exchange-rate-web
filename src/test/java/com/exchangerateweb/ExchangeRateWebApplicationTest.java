package com.exchangerateweb;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("integration")
public class ExchangeRateWebApplicationTest {
    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup() {
        stubFor(WireMock.get(urlMatching("/xml/30/01/2022"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                        .withBodyFile("xml/positive_response.xml")));

        stubFor(WireMock.get(urlMatching("/xml/31/01/2022"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)));

        stubFor(WireMock.get(urlMatching("/json/2022/01/30"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/javascript")
                        .withBodyFile("json/positive_response.json")));

        stubFor(WireMock.get(urlMatching("/json/2022/01/31"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/javascript")));
    }

    @Test
    public void happyPathXmlTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "AUD")
                        .param("date", "2022-01-30")
                        .param("dataSource", "xml"))
                .andExpect(content().string("47.6537"));
    }

    @Test
    public void happyPathJsonTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "AUD")
                        .param("date", "2022-01-30")
                        .param("dataSource", "json"))
                .andExpect(content().string("48.711"));
    }

    @Test
    public void noCurrencyFoundTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "XXX")
                        .param("date", "2022-01-30")
                        .param("dataSource", "json"))
                .andExpect(content().string("No such currency found."));
    }

    @Test
    public void noJsonDtoTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "AUD")
                        .param("date", "2022-01-31")
                        .param("dataSource", "json"))
                .andExpect(content().string("Unable to get exchange rate data from the source."));
    }

    @Test
    public void noXmlDtoTest() throws Exception {
        mockMvc.perform(get("/exchangerate")
                        .param("currency", "AUD")
                        .param("date", "2022-01-31")
                        .param("dataSource", "xml"))
                .andExpect(content().string("Unable to get exchange rate data from the source."));
    }
}
