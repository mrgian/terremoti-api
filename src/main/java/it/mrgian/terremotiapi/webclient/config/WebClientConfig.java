package it.mrgian.terremotiapi.webclient.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.ResourceUtils;

public class WebClientConfig {
    private String baseUrl;
    private String token;

    private ObjectMapper objectMapper;
    protected JsonNode jsonNode;

    public WebClientConfig(String baseUrl, String token) {
        this.setBaseUrl(baseUrl);
        this.setToken(token);
    }

    public WebClientConfig() {
        try {
            objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readValue(getDefaultConfigJson(), JsonNode.class);

            this.setBaseUrl(jsonNode.get("baseUrl").textValue());
            this.setToken(jsonNode.get("token").textValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDefaultConfigJson() {
        String config = "";

        try {
            File configFile = ResourceUtils.getFile("classpath:defaultConfig.json");
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

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

    public void setToken(String token) {
        this.token = token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}