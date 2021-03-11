package im.gian.terremoti.api.webclient.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import im.gian.terremoti.api.utils.FileUtils;

/**
 * Classe di configurazione per un webclient generico
 * 
 * @author Gianmatteo Palmieri
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
            jsonNode = objectMapper.readValue(FileUtils.readFile("/defaultConfig.json", getClass()), JsonNode.class);

            this.baseUrl = jsonNode.get("baseUrl").textValue();
            this.token = jsonNode.get("token").textValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}