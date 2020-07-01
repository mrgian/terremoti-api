package it.mrgian.terremotiapi.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}