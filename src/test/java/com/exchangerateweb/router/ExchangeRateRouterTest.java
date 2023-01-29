package com.exchangerateweb.router;

import com.exchangerateweb.service.ExchangeRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExchangeRateRouterTest {

    @Test
    public void happyPathTest() {
        ExchangeRateService firstService = mock(ExchangeRateService.class);
        when(firstService.getDataSource()).thenReturn(DataSource.JSON);

        ExchangeRateService secondService = mock(ExchangeRateService.class);
        when(secondService.getExchangeRate(any(), any())).thenReturn(300d);
        when(secondService.getDataSource()).thenReturn(DataSource.XML);

        ExchangeRateRouter underTest = new ExchangeRateRouter(List.of(firstService, secondService));
        assertEquals(300d,
                underTest.getExchangeRate("USD", LocalDate.parse("2022-01-01"), DataSource.XML));
        verify(secondService).getExchangeRate(eq("USD"), eq(LocalDate.parse("2022-01-01")));
    }

    @Test
    public void noDataSourceTest() {
        ExchangeRateService service = mock(ExchangeRateService.class);
        when(service.getDataSource()).thenReturn(DataSource.XML);
        ExchangeRateRouter underTest = new ExchangeRateRouter(List.of(service));
        RuntimeException e = Assertions.assertThrows(RuntimeException.class,
                () -> underTest.getExchangeRate("USD", LocalDate.parse("2022-01-01"), DataSource.JSON));
        assertEquals("No such data source found.", e.getMessage());
    }

    @Test
    public void invalidConfigurationTest() {
        ExchangeRateService firstService = mock(ExchangeRateService.class);
        when(firstService.getDataSource()).thenReturn(DataSource.JSON);

        ExchangeRateService secondService = mock(ExchangeRateService.class);
        when(secondService.getDataSource()).thenReturn(DataSource.JSON);

        Assertions.assertThrows(IllegalStateException.class,
                () -> new ExchangeRateRouter(List.of(firstService, secondService)));
    }
}
