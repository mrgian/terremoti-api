package it.mrgian.terremotiapi.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe che contiene metodi statici utili per operazioni sulle date
 */
public class DateUtils {

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