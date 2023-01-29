package com.exchangerateweb.converter;

import com.exchangerateweb.router.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DataSourceConverterTest {
    private DataSourceConverter underTest = new DataSourceConverter();

    @ParameterizedTest
    @MethodSource("makeDataSourceByString")
    public void happyPathTest(String sourceName, DataSource dataSource) {
        Assertions.assertEquals(dataSource, underTest.convert(sourceName));
    }

    @Test
    public void invalidSourceNameTest() {
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.convert("invalid"));
        Assertions.assertEquals("Unable to parse data source from invalid", e.getMessage());
    }

    private static Stream<Arguments> makeDataSourceByString() {
        return Stream.of(
                arguments("json", DataSource.JSON),
                arguments("JSON", DataSource.JSON),
                arguments("xml", DataSource.XML),
                arguments("XMl", DataSource.XML)
        );
    }
}
