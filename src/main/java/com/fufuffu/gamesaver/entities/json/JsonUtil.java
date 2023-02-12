package com.fufuffu.gamesaver.entities.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Optional<String> toJson(Object input) {
        try {
            return Optional.of(mapper.writeValueAsString(input));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static <T> Optional<T> fromJson(String jsonValue, Class<T> cls) {
        try {
            return Optional.of(mapper.readValue(jsonValue, cls));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static void configure(DeserializationFeature feature, boolean value) {
        mapper.configure(feature, value);
    }
}
