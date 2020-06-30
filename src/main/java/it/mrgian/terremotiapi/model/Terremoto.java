package it.mrgian.terremotiapi.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import org.apache.commons.lang.StringUtils;

import it.mrgian.terremotiapi.model.magnitudo.Magnitudo;
import it.mrgian.terremotiapi.model.magnitudo.TipoMagnitudo;

/**
 * Classe che gestisce le informazioni su un terremoto
 * 
 * @author Gianmatteo Palmieri
 */
public class Terremoto {
    @JsonPropertyDescription("Magnitudo del terremoto")
    private Magnitudo magnitudo;

    @JsonPropertyDescription("Ora del terremoto")
    private String ora;

    @JsonPropertyDescription("Data del terremoto")
    private String data;

    @JsonPropertyDescription("Luogo in cui è avvenuto il terremoto")
    private String localita;

    @JsonPropertyDescription("Profondità in km alla quale è avvenuto il terremoto")
    private float profondita;

    @JsonPropertyDescription("Link da seguire per maggiori informazioni sul terremoto")
    private String link;

    public Terremoto(String tweet) {
        parseTweet(tweet);
    }

    /**
     * Effettua il parsing del testo del tweet assegnando i valori ricavati alle
     * singole variabili
     * 
     * @param tweet Testo del tweet contenente le informazioni sul terremoto
     */
    private void parseTweet(String tweet) {
        try {
            // parsing magnitudo locale
            if (tweet.contains(" ML ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " ML ", " ore "));
                TipoMagnitudo tipo = TipoMagnitudo.ML;
                this.magnitudo = new Magnitudo(valore, tipo);
            }

            // parsing magnitudo momento
            if (tweet.contains(" Mw ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " Mw ", " ore "));
                TipoMagnitudo tipo = TipoMagnitudo.Mw;
                this.magnitudo = new Magnitudo(valore, tipo);
            }

            // parsing ora
            this.ora = StringUtils.substringBetween(tweet, " ore ", " IT ");

            // parsing data e località
            if (tweet.contains(" a ")) {
                this.data = StringUtils.substringBetween(tweet, " del ", " a ");
                this.localita = StringUtils.substringBetween(tweet, " a ", " Prof=");
            }
            if (tweet.contains(", ")) {
                this.data = StringUtils.substringBetween(tweet, " del ", ", ");
                this.localita = StringUtils.substringBetween(tweet, ", ", " Prof=");
            }

            // parsing profonodità
            this.profondita = Float.parseFloat(StringUtils.substringBetween(tweet, "Prof=", "Km #INGV"));

            // parsing link
            this.link = StringUtils.right(tweet, 23);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Magnitudo getMagnitudo() {
        return magnitudo;
    }

    public float getProfondita() {
        return profondita;
    }

    public String getLocalita() {
        return localita;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getLink() {
        return link;
    }

    public String toString() {
        String string = "Magnitudo: " + getMagnitudo().getTipo() + " " + getMagnitudo().getValore() + "\n";
        string += "Ora: " + getOra() + "\n";
        string += "Data: " + getData() + "\n";
        string += "Località: " + getLocalita() + "\n";
        string += "Profondità: " + getProfondita() + "\n";

        return string;
    }

}