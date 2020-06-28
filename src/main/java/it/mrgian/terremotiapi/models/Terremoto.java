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

    /**
     * Questo costruttore effettua il parsing assegnando alle singole variabili i
     * valori ricavati
     * 
     * @param tweet Testo del tweet contenente le informazioni sul terremoto
     */
    public Terremoto(String tweet) {
        this.magnitudo = Float.parseFloat(StringUtils.substringBetween(tweet, " ML ", " ore "));

        this.ora = StringUtils.substringBetween(tweet, " ore ", " IT ");

        this.data = null;
        this.localita = null;
        if (tweet.contains(" a ")) {
            this.data = StringUtils.substringBetween(tweet, " del ", " a ");
            this.localita = StringUtils.substringBetween(tweet, " a ", " Prof=");
        }
        if (tweet.contains(", ")) {
            this.data = StringUtils.substringBetween(tweet, " del ", ", ");
            this.localita = StringUtils.substringBetween(tweet, ",  ", " Prof=");
        }

        this.profondita = StringUtils.substringBetween(tweet, "Prof=", " #INGV");
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