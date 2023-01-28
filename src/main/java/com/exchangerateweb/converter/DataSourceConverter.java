package com.exchangerateweb.converter;

import com.exchangerateweb.router.DataSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DataSourceConverter implements Converter<String, DataSource> {

    @Override
    public DataSource convert(String source) {
        switch (source.toLowerCase()) {
            case "json" -> {
                return DataSource.JSON;
            }
            case "xml" -> {
                return DataSource.XML;
            }
            default -> throw new IllegalArgumentException("Unable to parse data source from " + source);
        }
    }
}
