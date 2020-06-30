package it.mrgian.terremotiapi.webclient.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebClientConfig {
    private String baseUrl;
    private String token;

    private ObjectMapper objectMapper;
    protected JsonNode jsonNode;

    public WebClientConfig(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public WebClientConfig() {
        try {
            objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readValue(getDefaultConfigJson(), JsonNode.class);

            this.baseUrl = jsonNode.get("baseUrl").textValue();
            this.token = jsonNode.get("token").textValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDefaultConfigJson() {
        String config = "";

        try {
            InputStreamReader stream = new InputStreamReader(getClass().getResourceAsStream("/defaultConfig.json"));
            BufferedReader reader = new BufferedReader(stream);

            String line;
            while ((line = reader.readLine()) != null)
                config += line + "\n";

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}