package it.mrgian.terremotiapi.webclient;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

public class TwitterWebClient {
    private TwitterWebClientConfig config;
    private WebClient webClient;

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