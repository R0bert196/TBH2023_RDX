package com.rdx.rdxserver.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class FloatArrayConverter implements AttributeConverter<float[], String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(float[] attribute) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        for (float f : attribute) {
            joiner.add(Float.toString(f));
        }
        return joiner.toString();
    }

    @Override
    public float[] convertToEntityAttribute(String dbData) {
        String[] stringValues = dbData.split(DELIMITER);
        float[] values = new float[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            values[i] = Float.parseFloat(stringValues[i]);
        }
        return values;
    }
}