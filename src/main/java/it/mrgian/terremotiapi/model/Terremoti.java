package it.mrgian.terremotiapi.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jamsesso.jsonlogic.JsonLogic;
import it.mrgian.terremotiapi.exception.InvalidFieldException;
import it.mrgian.terremotiapi.exception.InvalidFilterException;
import it.mrgian.terremotiapi.utils.JsonUtils;

/**
 * Classe che gestisce una lista di terremoti
 * 
 * @author Gianmatteo Palmieri
 */
public class Terremoti extends ArrayList<Terremoto> {

    private static final long serialVersionUID = 1L;

    public Terremoti() {
        super();
    }

    /**
     * Inizializza la lista aggiungendo i terremoti ricavati dalla risposta all'API
     * di Twitter
     * 
     * @param twitterResponse Risposta all'API di Twitter
     */
    public Terremoti(String twitterResponse) {
        addTerremotiFromTwitterResponse(twitterResponse);
    }

    /**
     * Restituisce la media dei terremoti al giorno
     * 
     * @return JSON contentente le statistiche
     */
    public String getStats() {
        float mediaGiorno = 0;

        ArrayList<Integer> countGiorni = new ArrayList<>();
        String lastDay = "";

        for (Terremoto terremoto : this) {
            if (!terremoto.getData().equals(lastDay))
                countGiorni.add(1);
            else
                countGiorni.set(countGiorni.size() - 1,
                        countGiorni.set(countGiorni.size() - 1, countGiorni.get(countGiorni.size() - 1)) + 1);

            lastDay = terremoto.getData();
        }

        for (Integer integer : countGiorni) {
            mediaGiorno += integer;
        }

        mediaGiorno /= countGiorni.size();

        HashMap<String, Object> map = new HashMap<>();
        map.put("mediaGiorno", mediaGiorno);

        return JsonUtils.mapToJson(map);
    }

    /**
     * Restituisce le statistiche di uno specifico campo sui terremoti
     * 
     * @return JSON contentente le statistiche
     */
    public String getStats(String field) throws InvalidFieldException {
        HashMap<String, Object> map = new HashMap<>();
        if (!field.equals("valoreMagnitudo") && !field.equals("profondita"))
            throw new InvalidFieldException("parametro non valido");

        float min = -1;
        float max = -1;
        float avg = 0;

        int counter = 0;

        try {
            for (Terremoto terremoto : this) {
                Method m = terremoto.getClass()
                        .getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
                float value = (float) m.invoke(terremoto);

                if (min == -1 || value < min)
                    min = value;

                if (max == -1 || value > max)
                    max = value;

                avg += value;

                counter++;
            }

            avg /= counter;
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("min", min);
        map.put("max", max);
        map.put("avg", avg);

        return JsonUtils.mapToJson(map);
    }

    /**
     * @param response Json restituito dalla chiamata l'API di Twitter contenente le
     *                 informazioni sui tweet
     */
    private void addTerremotiFromTwitterResponse(String response) {
        try {
            JsonNode tweets = new ObjectMapper().readTree(response);
            tweets.get("statuses").forEach(tweet -> {
                String tweetText = tweet.get("full_text").asText();
                if (tweetText.contains("[DATI #RIVISTI]"))
                    this.add(new Terremoto(tweetText));
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Questo metodo filtra la lista di terremoti secondo le regole passate nel
     * filtro. Itera la lista dei terremoti e per ogni terremoto verifica se le
     * regole specificate nel filtro sono rispettate.
     * 
     * @param filter filtro in formato JSON
     */
    public Terremoti filter(String filter) throws InvalidFilterException {
        JsonLogic jsonLogic = new JsonLogic();
        Terremoti filtered = new Terremoti();

        if (!JsonUtils.isValidJSON(filter))
            throw new InvalidFilterException("filtro non valido");

        try {
            for (Terremoto terremoto : this) {
                Method[] methods = terremoto.getClass().getMethods();
                Map<String, Object> data = new HashMap<String, Object>();
                for (Method m : methods) {
                    if (m.getName().startsWith("get")) {
                        Object value = m.invoke(terremoto);
                        String uppcaseName = m.getName().substring(3);
                        String name = uppcaseName.substring(0, 1).toLowerCase() + uppcaseName.substring(1);
                        data.put(name, value);
                    }
                }

                if ((boolean) jsonLogic.apply(filter, data))
                    filtered.add(terremoto);
            }
        } catch (Exception e) {
            throw new InvalidFilterException("filtro non valido");
        }

        return filtered;

    }
}