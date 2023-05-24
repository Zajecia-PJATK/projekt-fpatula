package com.business.market.simulator.utils;

import jakarta.persistence.AttributeConverter;

import java.math.BigDecimal;

public class BigDecimalToStringConverter implements AttributeConverter<BigDecimal, String> {
    @Override
    public String convertToDatabaseColumn(BigDecimal attribute) {
        return attribute.toString();
    }

    @Override
    public BigDecimal convertToEntityAttribute(String dbData) {
        return new BigDecimal(dbData);
    }
}
