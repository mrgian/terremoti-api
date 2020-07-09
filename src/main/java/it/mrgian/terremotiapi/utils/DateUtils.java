package it.mrgian.terremotiapi.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe che contiene metodi statici per operazioni sulle date
 * 
 * @author Gianmatteo Palmieri
 */
public class DateUtils {

    /**
     * Trasforma una data in formato dd-MM-yyyy in un int di formato yyyyMMdd
     * 
     * @param data Data in formato dd-MM-yyyy
     * @return data/ora in int formato yyyyMMdd
     */
    public static int dataToInt(String data) {
        String startString = (String) data;
        String finalString = startString.split("-")[2];
        finalString += startString.split("-")[1] + startString.split("-")[0];

        return Integer.parseInt(finalString);
    }

    /**
     * Metodo che incrementa una data di un giorno
     * 
     * @param date Data
     * @return Data incrementata di un giorno
     */
    public static Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}