package com.hb.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.util.Arrays;

public class Converter {
    public static String camelCaseToSnakeCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return input.replaceAll(regex, replacement).toLowerCase();
    }

    public static  JsonNode objectFromCamelCaseToSnakeCase(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        try {
            return mapper.readTree(mapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            String jsonString = "{\"key\":\"value\", \"nested\":{\"nested_key\":\"nested_value\"}}";

            return mapper.readTree(jsonString);
        }

    }
}
