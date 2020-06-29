package it.mrgian.terremotiapi.webclient.config;

import org.springframework.stereotype.Component;

@Component
public class TwitterWebClientConfig extends WebClientConfig {
    private String user;

    public TwitterWebClientConfig(String baseUrl, String token, String user) {
        super(baseUrl, token);
        this.setUser(user);
    }

    public TwitterWebClientConfig() {
        super();

        try {
            this.setUser(jsonNode.get("user").textValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}