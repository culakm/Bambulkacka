package com.hellbilling.bambulkacka;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

    private static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        return  new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
    }

    public static String timeDiff(String dateStart,String dateStop){
        if (dateStart == null || dateStop == null){
            return "Unknown time";
        }
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

    private static String timeDiff(Date dateStart, Date dateStop){
        long diff = dateStop.getTime() - dateStart.getTime();
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        //long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        //return String.valueOf(minutes);
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        return diffMinutes + ":" + diffSeconds;

    }

    // Random generatory
    public static int generateRandomInt(int parResultStart,int parResultStop){
        Random r = new Random();
        return r.nextInt(parResultStop - parResultStart + 1) + parResultStart;
    }

    private boolean generateRandomBool(){
        Random random = new Random();
        return random.nextBoolean();
    }

}
