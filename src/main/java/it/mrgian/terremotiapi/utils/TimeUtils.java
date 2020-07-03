package it.mrgian.terremotiapi.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe che contiene metodi statici utili per operazioni sulle date
 * 
 * @author Gianmatteo Palmieri
 */
public class TimeUtils {

    /**
     * Trasforma una data in un int di formato "yyyyMMdd"
     * 
     * @param time Object che rappresenta la data
     * @return data/ora in int formato "yyyyMMdd"
     */
    public static int dataToInt(Object data) {
        String startString = (String) data;
        String finalString = startString.split("-")[2];
        finalString += startString.split("-")[1] + startString.split("-")[0];

        return Integer.parseInt(finalString);
    }

    /**
     * Metodo che incrementa una data di un giorno
     * 
     * @param date data iniziale
     * @return data incrementata di un giorno
     */
    public static Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}