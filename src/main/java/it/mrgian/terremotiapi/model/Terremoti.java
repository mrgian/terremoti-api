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
     * Effettua e restituisce le statistiche sui terremoti
     * 
     * @return JSON contentente le statistiche
     */
    public String getStats() {
        float mediaMagnitudo = 0;
        float mediaProfondita = 0;
        float mediaGiorno = 0;

        ArrayList<Integer> countGiorni = new ArrayList<>();
        String lastDay = "";

        int counter = 0;
        for (Terremoto terremoto : this) {
            mediaMagnitudo += terremoto.getValoreMagnitudo();
            mediaProfondita += terremoto.getProfondita();

            if (!terremoto.getData().equals(lastDay))
                countGiorni.add(1);
            else
                countGiorni.set(countGiorni.size() - 1,
                        countGiorni.set(countGiorni.size() - 1, countGiorni.get(countGiorni.size() - 1)) + 1);

            lastDay = terremoto.getData();

            counter++;
        }

        mediaMagnitudo /= counter;
        mediaProfondita /= counter;

        for (Integer integer : countGiorni) {
            mediaGiorno += integer;
        }

        mediaGiorno /= countGiorni.size();

        return statsToJson(mediaMagnitudo, mediaProfondita, mediaGiorno);
    }

    /**
     * @return JSON con i parametri passati
     */
    private String statsToJson(float mediaMagnitudo, float mediaProfondita, float mediaGiorno) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mediaMagnitudo", mediaMagnitudo);
        map.put("mediaProfondita", mediaProfondita);
        map.put("mediaGiorno", mediaGiorno);

        String json = "";

        try {
            json = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
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
    public Terremoti filter(String filter) {
        JsonLogic jsonLogic = new JsonLogic();
        Terremoti filtered = new Terremoti();

        for (Terremoto terremoto : this) {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filtered;

    }
}