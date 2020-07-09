package it.mrgian.terremotiapi.webclient.config;

import org.springframework.stereotype.Component;

/**
 * Classe di configurazione del webclient che effettua richieste all'API di
 * Twitter.
 * 
 * @author Gianmatteo Palmieri
 */
@Component
public class TwitterWebClientConfig extends WebClientConfig {
    private String user;

    /**
     * Inizializza la configurazione con i parametri passati
     * 
     * @param baseUrl Url di base (Es.:
     *                https://api.twitter.com/1.1/search/tweets.json)
     * @param token   Token di accesso usato per l'autenticazione
     * @param user    Utente Twitter da cui scaricare i dati
     */
    public TwitterWebClientConfig(String baseUrl, String token, String user) {
        super(baseUrl, token);
        this.user = user;
    }

    /**
     * Inizializza la configurazione con i parametri di default presenti nel file
     * defaultConfig.json
     */
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