package it.mrgian.terremotiapi.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.mrgian.terremotiapi.model.Terremoto;

public class TerremotiUtils {

    /**
     * Effettua le statistiche sui terremoto
     * 
     * @param terremoti ArrayList dei terremoti su cui effettuare le statistiche
     * @return JSON contentente le statistiche
     */
    public static String getStatsTerremoti(ArrayList<Terremoto> terremoti) {
        float mediaMagnitudo = 0;
        float mediaProfondita = 0;
        float mediaGiorno = 0;

        ArrayList<Integer> countGiorni = new ArrayList<>();
        String lastDay = "";

        int counter = 0;
        for (Terremoto terremoto : terremoti) {
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
    private static String statsToJson(float mediaMagnitudo, float mediaProfondita, float mediaGiorno) {
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

    public static ArrayList<Terremoto> getTerremotiFromTwitterResponse(String response) {
        ArrayList<Terremoto> terremoti = new ArrayList<>();

        try {
            JsonNode tweets = new ObjectMapper().readTree(response);
            tweets.get("statuses").forEach(tweet -> {
                String tweetText = tweet.get("full_text").asText();
                if (tweetText.contains("[DATI #RIVISTI]"))
                    terremoti.add(new Terremoto(tweetText));
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return terremoti;
    }
}