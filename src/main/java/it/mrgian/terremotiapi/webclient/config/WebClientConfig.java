package it.mrgian.terremotiapi.webclient.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe di configurazione per un webclient generico
 */
public class WebClientConfig {
    private String baseUrl;
    private String token;

    private ObjectMapper objectMapper;
    protected JsonNode jsonNode;

    /**
     * Inizializza la configurazione con i parametri passati
     * 
     * @param baseUrl Url di base (Es.:
     *                https://api.twitter.com/1.1/search/tweets.json)
     * @param token   Token di accesso usato per l'autenticazione
     */
    public WebClientConfig(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    /**
     * Inizializza la configurazione con i parametri di default presenti nel file
     * defaultConfig.json
     */
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

    /**
     * @return il contenuto del file di configurazione di default
     */
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