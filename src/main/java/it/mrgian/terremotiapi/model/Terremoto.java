package it.mrgian.terremotiapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import org.apache.commons.lang.StringUtils;

import it.mrgian.terremotiapi.exception.MissingTweetFieldException;

/**
 * Classe che gestisce le informazioni su un terremoto
 * 
 * @author Gianmatteo Palmieri
 */
public class Terremoto {

    /**
     * Valore magnitudo del terremoto
     */
    @JsonPropertyDescription("Valore magnitudo del terremoto")
    private float valoreMagnitudo;

    /**
     * Tipo magnitudo del terremoto (Locale o momento)
     */
    @JsonPropertyDescription("Tipo magnitudo del terremoto")
    private String tipoMagnitudo;

    /**
     * Ora a cui è avvenuto il terremoto in formato "hh:mm"
     */
    @JsonPropertyDescription("Ora del terremoto")
    private String ora;

    /**
     * Data in cui è avvenuto il terremoto in formato "dd-MM-yyyy"
     */
    @JsonPropertyDescription("Data del terremoto")
    private String data;

    /**
     * Luogo in cui è avvenuto il terremoto
     */
    @JsonPropertyDescription("Luogo in cui è avvenuto il terremoto")
    private String localita;

    /**
     * Profondità espressa in km a cui è avvenuto il terremoto
     */
    @JsonPropertyDescription("Profondità in km alla quale è avvenuto il terremoto")
    private float profondita;

    /**
     * Link da seguire per avere maggiori informazioni sul terremoto
     */
    @JsonPropertyDescription("Link da seguire per maggiori informazioni sul terremoto")
    private String link;

    /**
     * Costruttore che effettua il parsing del testo del tweet per ricavarne
     * informazioni sul terremoto
     * 
     * @param tweet testo del tweet di cui effettuare il parsing
     */
    public Terremoto(String tweet) {
        try {
            parseTweet(tweet);
        } catch (MissingTweetFieldException e) {
            e.printStackTrace();
        }
    }

    @JsonCreator
    public Terremoto(@JsonProperty("valoreMagnitudo") float valoreMagnitudo,
            @JsonProperty("tipoMagnitudo") String tipoMagnitudo, @JsonProperty("ora") String ora,
            @JsonProperty("data") String data, @JsonProperty("localita") String localita,
            @JsonProperty("profondita") float profondita, @JsonProperty("link") String link) {
        this.valoreMagnitudo = valoreMagnitudo;
        this.tipoMagnitudo = tipoMagnitudo;
        this.ora = ora;
        this.data = data;
        this.localita = localita;
        this.profondita = profondita;
        this.link = link;
    }

    /**
     * Effettua il parsing del testo del tweet assegnando i valori ricavati alle
     * singole variabili
     * 
     * @param tweet Testo del tweet contenente le informazioni sul terremoto
     */
    private void parseTweet(String tweet) throws MissingTweetFieldException {
        try {
            // parsing magnitudo locale
            if (tweet.contains(" ML ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " ML ", " ore "));
                String tipo = "ML";
                this.valoreMagnitudo = valore;
                this.tipoMagnitudo = tipo;
            }
            // parsing magnitudo momento
            else if (tweet.contains(" Mw ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " Mw ", " ore "));
                String tipo = "Mw";
                this.valoreMagnitudo = valore;
                this.tipoMagnitudo = tipo;
            } else
                throw new MissingTweetFieldException("Valore magnitudo mancante");

            // parsing ora
            this.ora = StringUtils.substringBetween(tweet, " ore ", " IT ");
            if (this.ora == null)
                throw new MissingTweetFieldException("Ora mancante");

            // parsing data e località
            if (tweet.contains(" a ")) {
                this.data = StringUtils.substringBetween(tweet, " del ", " a ");
                this.localita = StringUtils.substringBetween(tweet, " a ", " Prof=");
            } else if (tweet.contains(", ")) {
                this.data = StringUtils.substringBetween(tweet, " del ", ", ");
                this.localita = StringUtils.substringBetween(tweet, ", ", " Prof=");
            } else
                throw new MissingTweetFieldException("Data o località mancante");

            // parsing profonodità
            this.profondita = -1;
            this.profondita = Float.parseFloat(StringUtils.substringBetween(tweet, "Prof=", "Km #INGV"));
            if (this.profondita == -1)
                throw new MissingTweetFieldException("Valore profondità mancante");

            // parsing link
            this.link = StringUtils.right(tweet, 23);
            if (!this.link.contains("https"))
                throw new MissingTweetFieldException("Link mancante");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public float getValoreMagnitudo() {
        return valoreMagnitudo;
    }

    public String getTipoMagnitudo() {
        return tipoMagnitudo;
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
        String string = "Magnitudo: " + getTipoMagnitudo() + " " + getValoreMagnitudo() + "\n";
        string += "Ora: " + getOra() + "\n";
        string += "Data: " + getData() + "\n";
        string += "Località: " + getLocalita() + "\n";
        string += "Profondità: " + getProfondita() + "\n";

        return string;
    }

}