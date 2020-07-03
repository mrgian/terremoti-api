package it.mrgian.terremotiapi.webclient;

import it.mrgian.terremotiapi.exception.InvalidFieldException;
import it.mrgian.terremotiapi.model.Terremoti;
import it.mrgian.terremotiapi.utils.DateUtils;
import it.mrgian.terremotiapi.utils.ErrorUtils;
import it.mrgian.terremotiapi.utils.JsonUtils;
import it.mrgian.terremotiapi.webclient.config.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
     * Questo costruttore inizializza il web client
     * 
     * @param config Configurazione contentente baseUrl, token e user
     * @see it.mrgian.terremotiapi.webclient.TwitterWebClientConfig
     */
    public TwitterWebClient(TwitterWebClientConfig config) {
        this.config = config;

        webClient = WebClient.builder().baseUrl(config.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + config.getToken()).build();
    }

    /**
     * @return ArrayList dei terremoti degli ultimi 7 giorni
     */
    public Terremoti getLatestTerremoti() {
        return getTerremoti(""); // nessun parametro aggiuntivo
    }

    public Terremoti getLatestFilteredTerremoti(String filter) throws Exception {
        try {
            return getLatestTerremoti().filter(filter);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @return Statistiche sui terremoti degli ultimi sette giorni in formato JSON
     */
    public String getStatsLatestTerremoti() {
        return getLatestTerremoti().getStats();
    }

    /**
     * @return Statistiche sui terremoti degli ultimi sette giorni di uno specifico
     *         campo in formato JSON
     */
    public String getStatsLatestTerremoti(String field) throws InvalidFieldException {
        try {
            return getLatestTerremoti().getStats(field);
        } catch (InvalidFieldException e) {
            e.printStackTrace();
            throw new InvalidFieldException(e.getMessage());
        }
    }

    /**
     * Questo metodo ritorna la lista dei terremoti avvenuti in una specifica a
     * data. A causa delle limitazioni delle API standard di Twitter questo metodo
     * non restituisce alcun dato se la data è più lontana di 7 giorni dal giorno in
     * cui viene invocato. (L'API standard di Twitter cerca e restituisce solo i
     * tweet degli ultimi 7 giorni)
     * 
     * @param dateString
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
     * E' possibile passare parametri aggiuntivi alla query di ricerca
     * 
     * @param params
     * @return ArrayList dei terremoti degli ultimi 7 giorni
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