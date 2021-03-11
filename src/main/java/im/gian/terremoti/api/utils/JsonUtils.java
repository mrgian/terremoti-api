package im.gian.terremoti.api.utils;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe che contiente il metodi statici per operazioni sui JSON
 * 
 * @author Gianmatteo Palmieri
 */
public class JsonUtils {
    /**
     * Trasforma una HashMap in una stringa in formato JSON
     * 
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

    /**
     * Verifica se il JSON passato è valido
     * 
     * @param json
     */
    public static boolean isValidJSON(String json) {
        try {
            new ObjectMapper().readTree(json);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}