package it.mrgian.terremotiapi.webclient;

import it.mrgian.terremotiapi.model.Terremoti;
import it.mrgian.terremotiapi.utils.DateUtils;
import it.mrgian.terremotiapi.webclient.config.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

/**
 * Classe per gestire il web client che effettua le richeste all'API di Twitter
 * 
 * @author Gianmatteo Palmieri
 */
@Service
public class TwitterWebClient implements it.mrgian.terremotiapi.webclient.WebClient {
    private TwitterWebClientConfig config;
    private WebClient webClient;

    /**
     * Inizializza il web client
     * 
     * @param config Configurazione contentente baseUrl, token e user
     */
    public TwitterWebClient(TwitterWebClientConfig config) {
        this.config = config;

        webClient = WebClient.builder().baseUrl(config.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + config.getToken()).build();
    }

    /**
     * @return Lista dei terremoti degli ultimi 7 giorni
     */
    public Terremoti getLatestTerremoti() {
        return getTerremoti(""); // nessun parametro aggiuntivo
    }

    /**
     * Ritorna la lista dei terremoti avvenuti in una specifica data. A causa delle
     * limitazioni delle API standard di Twitter questo metodo non restituisce alcun
     * dato se la data è più lontana di 7 giorni dal giorno in cui viene invocato.
     * (L'API standard di Twitter cerca e restituisce solo i tweet degli ultimi 7
     * giorni)
     * 
     * @param dateString Data in formato yyyy-MM-dd
     * @return ArrayList dei terremoti avvenuti in una specifica data
     */
    public Terremoti getDateTerremoti(String dateString) {
        Terremoti terremoti = new Terremoti();

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);

            String params = " since:" + dateFormat.format(date) + " until:"
                    + dateFormat.format(DateUtils.addOneDay(date));
            terremoti = getTerremoti(params);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return terremoti;
    }

    /**
     * Effettua una richiesta all'API di Twitter per ricevere la lista dei tweet
     * postati dall'utente specificato nella configurazione e convertirla in una
     * lista di terremoti.
     * 
     * @param params Parametri aggiuntivi
     * @return Lista dei terremoti degli ultimi 7 giorni
     */
    public Terremoti getTerremoti(String params) {
        Terremoti terremoti = new Terremoti();

        try {
            ResponseSpec response;

            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("q", "from:" + config.getUser() + params)
                            .queryParam("tweet_mode", "extended").queryParam("include_entities", "false")
                            .queryParam("count", 100).queryParam("trim_user", true).queryParam("result_type", "recent")
                            .build())
                    .retrieve();

            String responseString = response.bodyToMono(String.class).block();

            terremoti = new Terremoti(responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return terremoti;
    }

    public TwitterWebClientConfig getConfig() {
        return config;
    }
}