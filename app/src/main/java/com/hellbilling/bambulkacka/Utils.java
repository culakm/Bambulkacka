package com.hellbilling.bambulkacka;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Utilities for whole project
 * <p>
 * Time and Random generator methods.
 */
public class Utils {
    /**
     * Current time
     * @return Time as string
     */
    public static String getNow(){
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Time now + minutes
     * @param minutes minutes added to current time
     * @return Time as string
     */
    public static String getNowPlus(int minutes){
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = addMinutesToDate(minutes, new Date());
        return dateFormat.format(date);
    }

    /**
     * ??????
     * @param minutes
     * @param beforeTime
     * @return Date
     */
    private static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        return  new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
    }

    /**
     * ??????
     * @param dateStart
     * @param dateStop
     * @return MINS???
     */
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

    /**
     * ??????
     * @param dateStart
     * @param dateStop
     * @return MINS string???
     */
    private static String timeDiff(Date dateStart, Date dateStop){
        long diff = dateStop.getTime() - dateStart.getTime();
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        //long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        //return String.valueOf(minutes);
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        return diffMinutes + ":" + diffSeconds;

    }

    // Number handling
    /**
     * Find length of a number
     * @param number
     * @return length of the number
     */
    public static int lengthDigit(int number){
        return String.valueOf(number).length();
    }

    /**
     * Find out last numbers from first digit of a number
     * zisti posledne cislice od prvej cislice number
     * @param number
     * @return ???
     */
    public static int lastDigit(int number){
        int length = lengthDigit(number);
        int power;
        if (length == 1) {
            power = length;
        }
        else {
            power = length - 1;
        }
        int mod = (int) Math.pow(10,power);
        return number % mod;
    }

    // zisti posledne cislice od zadanej cislice 10^power
    /**
     * Find out ??
     * zisti posledne cislice od zadanej cislice 10^power
     *
     * @param number
     * @return ???
     */
    public static int lastDigit(int number, int power){
        int mod = (int) Math.pow(10,power);
        return number % mod;
    }

    /**
     * Change last digit(s) of number by lastDigit
     * @param number number to change
     * @param lastDigit new last digit(s)
     * @return new number
     */
    private static int changeDigit (int number, int lastDigit){
        String stringNumber = String.valueOf(number);
        String stringlastDigit = String.valueOf(lastDigit);

        int lengthNumber = stringNumber.length();
        int lengthLastDigit = stringlastDigit.length();

        int endNewNumber;
        if (lengthNumber - lengthLastDigit < 0){
            endNewNumber = lengthNumber;
        }
        else {
            endNewNumber = lengthNumber - lengthLastDigit;
        }

        String newNumber = stringNumber.substring(0,endNewNumber)+stringlastDigit;
        return Integer.valueOf(newNumber);
    }

    // vytvori pole vsetkych cislic v num

    /**
     * Create array of all digits in number
     * @param number
     * @return array of digits
     */
    private List<Integer> splitNumber (int number){

        List<Integer> list = new ArrayList<>();
        String numStr = Integer.toString(number);

        for(int i=0;i<numStr.length();i++)
        {
            list.add(Character.getNumericValue(numStr.charAt(i)));
        }
        return list;
    }
    // Random generators
    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int generateRandomInt(int min,int max){
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    /**
     * Generate random whole decimal number, limits are 10 to 100
     * @param min Minimum value, min here is 10
     * @param max Maximum value. max here is 90, Must be greater than min.
     * @return random int
     */
    public static int generateRandomIntWhole10(int min, int max){
        return generateRandomInt(min / 10, max / 10) * 10;
    }

    /**
     * Generate random decimal number but not whole decimal number
     * @param min Minimum value, min here is 10
     * @param max Maximum value. max here is 99, Must be greater than min.
     * @return random int
     */
    public static int generateRandomIntNot10s(int min,int max){
        int number;
        do {
            number = generateRandomInt(min, max);
        }
        while (lastDigit(number) == 0);
        return number;
    }

    /**
     * Generate random boolean
     * @return random boolean
     */
    private boolean generateRandomBool(){
        Random random = new Random();
        return random.nextBoolean();
    }

    /**
     *
     * @param signs
     * @return random sign
     */
    public static String generateRandomSign(ArrayList<String>  signs){
        // Load random sign from signs
        return signs.get(generateRandomInt(0, signs.size() - 1));
    }
}
