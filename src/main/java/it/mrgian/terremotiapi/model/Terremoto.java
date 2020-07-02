package it.mrgian.terremotiapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import org.apache.commons.lang.StringUtils;

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
        parseTweet(tweet);
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
    private void parseTweet(String tweet) {
        try {
            // parsing magnitudo locale
            if (tweet.contains(" ML ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " ML ", " ore "));
                String tipo = "ML";
                this.valoreMagnitudo = valore;
                this.tipoMagnitudo = tipo;
            }

            // parsing magnitudo momento
            if (tweet.contains(" Mw ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " Mw ", " ore "));
                String tipo = "Mw";
                this.valoreMagnitudo = valore;
                this.tipoMagnitudo = tipo;
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