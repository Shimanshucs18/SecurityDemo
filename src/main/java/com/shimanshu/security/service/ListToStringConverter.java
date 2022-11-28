package com.shimanshu.security.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class ListToStringConverter implements AttributeConverter<Set<String>, String> {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }



    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        if (null == attribute) {
            // You may return null if you prefer that style
            return "{}";
        }

        try {
            return objectMapper.writeValueAsString(attribute);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        if (null == dbData) {
            // You may return null if you prefer that style
            return new HashSet<>();
        }

        try {
            return objectMapper.readValue(dbData, new TypeReference<Set<String>>() {});

        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
