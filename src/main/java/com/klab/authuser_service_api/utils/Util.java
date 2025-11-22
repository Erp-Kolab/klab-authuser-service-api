package com.klab.authuser_service_api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
    public static String objectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    return  json;
    }
}
