package it.mrgian.terremotiapi.utils;

import java.util.HashMap;

public class ErrorUtils {
    public static String errorJson(Exception e) {
        HashMap<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("errore", e.getMessage());
        return JsonUtils.mapToJson(errorMap);
    }
}