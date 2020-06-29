package it.mrgian.terremotiapi.model;

import org.apache.commons.lang.StringUtils;

import it.mrgian.terremotiapi.model.magnitudo.Magnitudo;
import it.mrgian.terremotiapi.model.magnitudo.TipoMagnitudo;

/**
 * Classe che gestisce le informazioni su un terremoto
 * 
 * @author Gianmatteo Palmieri
 */
public class Terremoto {
    private Magnitudo magnitudo;
    private String ora;
    private String data;
    private String localita;
    private float profondita;

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
        try {
            // parsing magnitudo locale
            if (tweet.contains(" ML ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " ML ", " ore "));
                TipoMagnitudo tipo = TipoMagnitudo.ML;
                setMagnitudo(valore, tipo);
            }

            // parsing magnitudo momento
            if (tweet.contains(" Mw ")) {
                float valore = Float.parseFloat(StringUtils.substringBetween(tweet, " Mw ", " ore "));
                TipoMagnitudo tipo = TipoMagnitudo.Mw;
                setMagnitudo(valore, tipo);
            }

            // parsing ora
            setOra(StringUtils.substringBetween(tweet, " ore ", " IT "));

            // parsing data e località
            if (tweet.contains(" a ")) {
                setData(StringUtils.substringBetween(tweet, " del ", " a "));
                setLocalita(StringUtils.substringBetween(tweet, " a ", " Prof="));
            }
            if (tweet.contains(", ")) {
                setData(StringUtils.substringBetween(tweet, " del ", ", "));
                setLocalita(StringUtils.substringBetween(tweet, ", ", " Prof="));
            }

            // parsing profonodità
            setProfondita(Float.parseFloat(StringUtils.substringBetween(tweet, "Prof=", "Km #INGV")));

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

    public void setProfondita(float profondita) {
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

    public void setMagnitudo(float valore, TipoMagnitudo tipo) {
        this.magnitudo = new Magnitudo(valore, tipo);
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