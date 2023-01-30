package com.exchangerateweb.converter;

import com.exchangerateweb.router.DataSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataSourceConverter implements Converter<String, DataSource> {
    private final Map<String, DataSource> dataSourceMap = Arrays.stream(DataSource.values())
            .collect(Collectors.toMap(dataSource -> dataSource.name().toLowerCase(), Function.identity()));

    @Override
    public DataSource convert(String source) {
        source = source.toLowerCase();
        if (!dataSourceMap.containsKey(source)) {
            throw new IllegalArgumentException("Unable to parse data source from " + source);
        }
        return dataSourceMap.get(source);
    }
}
