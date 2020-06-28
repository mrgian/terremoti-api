package it.mrgian.terremotiapi.webclient;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

/**
 * Classe per gestire il web client che effettua le richeste all'API di Twitter
 * 
 * @author Gianmatteo Palmieri
 */
public class TwitterWebClient {
    private TwitterWebClientConfig config;
    private WebClient webClient;

    /**
     * Questo costruttore inizializza il web client
     * 
     * @param config Configurazione contentente baseUrl, token e user
     * @see it.mrgian.terremotiapi.webclient.TwitterWebClientConfig
     */
    public TwitterWebClient(TwitterWebClientConfig config) {
        this.setConfig(config);

        webClient = WebClient.builder().baseUrl(config.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + config.getToken()).build();
    }

    public List<String> getLatestTerremotiTweets() {
        List<String> tweets = new ArrayList<>();

        try {
            ResponseSpec response;

            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("q", "from:" + config.getUser())
                            .queryParam("tweet_mode", "extended").queryParam("include_entities", "false")
                            .queryParam("count", 100).queryParam("trim_user", true).build())
                    .retrieve();

            String responseString = response.bodyToMono(String.class).block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode;
            jsonNode = objectMapper.readTree(responseString);
            jsonNode.get("statuses").forEach(node -> tweets.add(node.get("full_text").asText()));

        } catch (Exception e) {

        }

        return tweets;
    }

    public TwitterWebClientConfig getConfig() {
        return config;
    }

    public void setConfig(TwitterWebClientConfig config) {
        this.config = config;
    }
}