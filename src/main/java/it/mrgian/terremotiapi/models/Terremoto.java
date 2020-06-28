package it.mrgian.terremotiapi.models;

import org.apache.commons.lang.StringUtils;

/**
 * Classe che gestisce le informazioni su un terremoto
 * 
 * @author Gianmatteo Palmieri
 */
public class Terremoto {
    private float magnitudo;
    private String ora;
    private String data;
    private String localita;
    private String profondita;

    public Terremoto(String tweet) {
        parseTweet(tweet);
    }

    /**
     * Effettua il parsing del testo del tweet assegnando i valori ricavati alle
     * singole variabili
     * 
     * @param tweet Testo del tweet contenente le informazioni sul terremoto
     */
    void parseTweet(String tweet) {
        setMagnitudo(Float.parseFloat(StringUtils.substringBetween(tweet, " ML ", " ore ")));

        setOra(StringUtils.substringBetween(tweet, " ore ", " IT "));

        setData(null);
        setLocalita(null);
        if (tweet.contains(" a ")) {
            setData(StringUtils.substringBetween(tweet, " del ", " a "));
            setLocalita(StringUtils.substringBetween(tweet, " a ", " Prof="));
        }
        if (tweet.contains(", ")) {
            setData(StringUtils.substringBetween(tweet, " del ", ", "));
            setLocalita(StringUtils.substringBetween(tweet, ",  ", " Prof="));
        }

        setProfondita(StringUtils.substringBetween(tweet, "Prof=", " #INGV"));
    }

    public float getMagnitudo() {
        return magnitudo;
    }

    public String getProfondita() {
        return profondita;
    }

    public void setProfondita(String profondita) {
        this.profondita = profondita;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public void setMagnitudo(float magnitudo) {
        this.magnitudo = magnitudo;
    }

}