package it.mrgian.terremotiapi.webclient.config;

import java.io.*;

import com.fasterxml.jackson.databind.*;

import org.springframework.util.ResourceUtils;

/**
 * Classe per la gestione della configurazione del web client
 * 
 * @author Gianmatteo Palmieri
 */
public class TwitterWebClientConfig {
    private String baseUrl;
    private String token;
    private String user;

    /**
     * Questo costruttore viene utilizzato quando vengono forniti il baseUrl e il
     * token
     * 
     * @param baseUrl Url base della API di Twitter
     * @param token   Token per l'autenticazione alla API di Twitter
     * @param user    Nome utente Twitter dai cui scaricare i tweet
     */
    public TwitterWebClientConfig(String baseUrl, String token, String user) {
        this.setBaseUrl(baseUrl);
        this.setToken(token);
        this.setUser(user);
    }

    /**
     * Questo costruttore viene utilizzato quando NON vengono forniti il baseUrl e
     * il token e vengono utilizzati quelli di default <br>
     * I valori di default vengono letti dal file JSON <b>defaultConfig.json</b>
     * presente nella directory <b>resources</b> del progetto.
     */
    public TwitterWebClientConfig() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode;
            jsonNode = objectMapper.readValue(getDefaultConfigJson(), JsonNode.class);

            this.setBaseUrl(jsonNode.get("baseUrl").textValue());
            this.setToken(jsonNode.get("token").textValue());
            this.setUser(jsonNode.get("user").textValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per leggere dal file <b>defaultConfig.json</b> il baseUrl e il token
     * di default
     * 
     * @return il contenuto del file <b>defaultConfig.json</b>
     */
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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}