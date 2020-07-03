package it.mrgian.terremotiapi.utils;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    /**
     * @return JSON con i parametri passati
     */
    public static String mapToJson(HashMap<String, Object> map) {
        String json = "";

        try {
            json = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static boolean isValidJSON(String json) {
        try {
            new ObjectMapper().readTree(json);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}