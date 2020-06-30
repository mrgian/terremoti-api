package it.mrgian.terremotiapi.webclient.config;

import org.springframework.stereotype.Component;

@Component
public class TwitterWebClientConfig extends WebClientConfig {
    private String user;

    public TwitterWebClientConfig(String baseUrl, String token, String user) {
        super(baseUrl, token);
        this.user = user;
    }

    public TwitterWebClientConfig() {
        super();

        try {
            this.user = jsonNode.get("user").textValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }
}