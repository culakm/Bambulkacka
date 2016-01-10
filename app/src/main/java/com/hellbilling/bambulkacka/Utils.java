package com.hellbilling.bambulkacka;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by mculak on 1/9/16.
 */
public class Utils {
    public static String getNow(){
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getNowPlus(int minutes){
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = addMinutesToDate(minutes, new Date());
        return dateFormat.format(date);
    }

    public static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static String timeDiff(String dateStart,String dateStop){
        // Convert String into Date
        // Custom date format
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDiff(d1,d2);
    }

    public static String timeDiff(Date dateStart,Date dateStop){
        long diff = dateStop.getTime() - dateStart.getTime();
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        //long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        //return String.valueOf(minutes);
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        return diffMinutes + ":" + diffSeconds;

    }
}
