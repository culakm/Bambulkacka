package com.hellbilling.bambulkacka;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Parcelable je implemetnovane aby sa objekt priklad dal prehadzovat cez onSaveInstanceState
// parcerable som ziskal hodenim kodu do http://www.parcelabler.com/
/**
 * Generates example for calculator
 */
public class Example implements Parcelable {

    private final int resultStart;
    private final int resultStop;
    private final int numberStart;
    private final int numberStop;
    private String sign = "all";
    /**
     * Extra conditions
     * nic, cez 10, multi10, dalsie
     */
    private String extra = "nic";
    private int a;
    private int b;
    private int result;
    private int wantedNumber;

    /**
     * Constructor without sign
     */
    private Example(int parResultStart, int parResultStop, int parNumberStart, int parNumberStop) {

        this.resultStart = parResultStart;
        this.resultStop = parResultStop;
        this.numberStart = parNumberStart;
        this.numberStop = parNumberStop;
    }

    /**
     * Constructor with sign
     */
    private Example(int parResultStart, int parResultStop, int parNumberStart, int parNumberStop, String parSign) {

        this(parResultStart,parResultStop,parNumberStart,parNumberStop);
        this.sign = parSign;
    }

    /**
     * Constructor with sign and extra
     */
    public Example(int parResultStart, int parResultStop, int parNumberStart, int parNumberStop, String parSign, String parExtra) {

        this(parResultStart,parResultStop,parNumberStart,parNumberStop,parSign);
        this.extra = parExtra;
    }
//////////////////////////////
    /**
     * Load all values into Example
     *
     */
    public void getNumbers() {
        // Random sign
        if (sign.equals("all")) {
            // Random for nasobilka
            if (extra.equals("multi10")) {
                sign = generatemulti10Sign();
            }
            else {
                sign = generateSign();
            }
        }

        // Random a
        a = generateRandomNumber();

        // + cast
        if (sign.equals("+")) {
            do {
                // extra
                if (extra.equals("cez 10")){
                    int aLastDigit;
                    int bLastDigit;
                    int power;
                    do {
                        aLastDigit = lastDigit(a);
                    } while (aLastDigit == 0);

                    do {
                        // Random b
                        b = generateRandomNumber();
                        // a inkrementujem aby rychlejsie narastlo do 10
                        a++;
                        if (lengthDigit(a) != lengthDigit(b)){
                            int minLength = Math.min(lengthDigit(a), lengthDigit(b));
                            power = minLength - 1;
                            aLastDigit = lastDigit(a,power);
                            bLastDigit = lastDigit(b,power);
                        }
                        else {
                            power = lengthDigit(a) - 1;
                            bLastDigit = lastDigit(b);
                        }
                    } while (aLastDigit + bLastDigit < Math.pow(10,power));
                } // normal
                else {
                    a = generateRandomNumber();
                    b = generateRandomNumber();
                }

                result = a + b;
                Log.d("priklad", "sign: " + sign + ", a: " + a + ", b: " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
            } while ( !(result >= resultStart && result <= resultStop) );

        } // - cast
        else if (sign.equals("-")) {
            do {
                b = generateRandomNumber();

                // vymen a a b ak je a mensie ako b
                if (a < b){
                    int c = a;
                    a = b;
                    b = c;
                }

                if (extra.equals("cez 10")){
                    int aLastDigit = lastDigit(a);
                    int bLastDigit = lastDigit(b);

                    if (aLastDigit > bLastDigit){
                        a = changeDigit(a,bLastDigit);
                        b = changeDigit(b,aLastDigit);
                    }
                }
                result = a - b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( (a < b) || (!(result >= resultStart && result <= resultStop))  );
        } // * cast
        else if (sign.equals("*")) {
            do {
                if (extra.equals("multi10")){
                    a = Utils.generateRandomInt(this.numberStart, 10);
                    if ( a <= this.numberStop ){
                        b = Utils.generateRandomInt(this.numberStart, 10);
                    }
                    else {
                        b = generateRandomNumber();
                    }
                }
                else{
                    b = generateRandomNumber();
                }

                result = a * b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( !(result >= resultStart && result <= resultStop)  );
        }
        else if (sign.equals("/")) {
            do {
                int divisionNumberStart = 1;
                if (extra.equals("multi10")){
                    a = Utils.generateRandomInt(divisionNumberStart, 10);
                    if ( a <= this.numberStop ){
                        b = Utils.generateRandomInt(divisionNumberStart, 10);
                    }
                    else {
                        b = Utils.generateRandomInt(divisionNumberStart, this.numberStop);
                    }
                    // b has to be lower than a
                    if (b > this.numberStop && b > a){
                        int c = b;
                        b = a;
                        a = c;
                    }
                }
                else {
                    a = Utils.generateRandomInt(divisionNumberStart, this.numberStop);
                    b = Utils.generateRandomInt(divisionNumberStart, this.numberStop);
                }

                int semi_result = a * b;
                a = semi_result;
                result = semi_result / b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( !(result >= resultStart && result <= resultStop)  );
        }

    }

/////////////////////////////
    /**
     * Load all values into Example
     * ORIG !!!
     */
    public void getNumbers2() {
        // Random sign
        if (sign.equals("all")) {
            // Random for nasobilka
            if (extra.equals("multi10")) {
                sign = generatemulti10Sign();
            }
            else {
                sign = generateSign();
            }
        }

        // Random a
        a = generateRandomNumber();

        // + cast
        if (sign.equals("+")) {
            do {
                // extra
                if (extra.equals("cez 10")){
                    int aLastDigit;
                    int bLastDigit;
                    int power;
                    do {
                        aLastDigit = lastDigit(a);
                    } while (aLastDigit == 0);

                    do {
                        // Random b
                        b = generateRandomNumber();
                        // a inkrementujem aby rychlejsie narastlo do 10
                        a++;
                        if (lengthDigit(a) != lengthDigit(b)){
                            int minLength = Math.min(lengthDigit(a), lengthDigit(b));
                            power = minLength - 1;
                            aLastDigit = lastDigit(a,power);
                            bLastDigit = lastDigit(b,power);
                        }
                        else {
                            power = lengthDigit(a) - 1;
                            bLastDigit = lastDigit(b);
                        }
                    } while (aLastDigit + bLastDigit < Math.pow(10,power));
                } // normal
                else {
                    a = generateRandomNumber();
                    b = generateRandomNumber();
                }

                result = a + b;
                Log.d("priklad", "sign: " + sign + ", a: " + a + ", b: " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
            } while ( !(result >= resultStart && result <= resultStop) );

        } // - cast
        else if (sign.equals("-")) {
            do {
                b = generateRandomNumber();

                // vymen a a b ak je a mensie ako b
                if (a < b){
                    int c = a;
                    a = b;
                    b = c;
                }

                if (extra.equals("cez 10")){
                    int aLastDigit = lastDigit(a);
                    int bLastDigit = lastDigit(b);

                    if (aLastDigit > bLastDigit){
                        a = changeDigit(a,bLastDigit);
                        b = changeDigit(b,aLastDigit);
                    }
                }
                result = a - b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( (a < b) || (!(result >= resultStart && result <= resultStop))  );
        } // * cast
        else if (sign.equals("*")) {
            do {
                if (extra.equals("multi10")){
                    a = Utils.generateRandomInt(this.numberStart, 10);
                    if ( a <= this.numberStop ){
                        b = Utils.generateRandomInt(this.numberStart, 10);
                    }
                    else {
                        b = generateRandomNumber();
                    }
                }
                else{
                    b = generateRandomNumber();
                }

                result = a * b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( !(result >= resultStart && result <= resultStop)  );
        }
        else if (sign.equals("/")) {
            do {
                int divisionNumberStart = 1;
                if (extra.equals("multi10")){
                    a = Utils.generateRandomInt(divisionNumberStart, 10);
                    if ( a <= this.numberStop ){
                        b = Utils.generateRandomInt(divisionNumberStart, 10);
                    }
                    else {
                        b = Utils.generateRandomInt(divisionNumberStart, this.numberStop);
                    }
                    // b has to be lower than a
                    if (b > this.numberStop && b > a){
                        int c = b;
                        b = a;
                        a = c;
                    }
                }
                else {
                    a = Utils.generateRandomInt(divisionNumberStart, this.numberStop);
                    b = Utils.generateRandomInt(divisionNumberStart, this.numberStop);
                }

                int semi_result = a * b;
                a = semi_result;
                result = semi_result / b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( !(result >= resultStart && result <= resultStop)  );
        }

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

    /**
     * Find length of a number
     * @param number
     * @return length of the number
     */
    private static int lengthDigit(int number){
        return String.valueOf(number).length();
    }

    /**
     * Find out last numbers from first digit of a number
     * zisti posledne cislice od prvej cislice number
     * @param number
     * @return ???
     */
    private static int lastDigit(int number){
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
    private static int lastDigit(int number, int power){
        int mod = (int) Math.pow(10,power);
        return number % mod;
    }

    // vytvori pole vsetkych cislic v num

    /**
     * Create array of all digits in number
     * @param number
     * @return array of digits
     */
    private List<Integer> splitNumer (int number){

        List<Integer> list = new ArrayList<>();
        String numStr = Integer.toString(number);

        for(int i=0;i<numStr.length();i++)
        {
            list.add(Character.getNumericValue(numStr.charAt(i)));
        }
        return list;
    }

    // Gets
    int getA (){
        return a;
    }

    int getB (){
        return b;
    }

    int getResult(){
        return result;
    }

    int getWantedNumber(){
        return wantedNumber;
    }

    String getSign(){
        return sign;
    }

    String getExtra (){
        return extra;
    }

    String getExampleString() {  return a + sign + b + "="; }

    String getExampleStringFull() { return getExampleString() + result; }

    /**
     * Generate random number in numberStart, numberStop
     * @return random int
     */
    private int generateRandomNumber(){
        return Utils.generateRandomInt(this.numberStart, this.numberStop);
    }

    /**
     * Random generator of sign for all signs
     * 0 = +
     * 1 = -
     * 2 = *
     * 3 = /
     * Default +
     * @return sign as string +,-,*,/
     */
    private String generateSign(){

        Random r = new Random();
        int randomInt = r.nextInt(4);
        Log.d("sign int","generateSign = "+randomInt);
        String sign;
        switch (randomInt) {
            case 0:  sign = "+";
                break;
            case 1:  sign = "-";
                break;
            case 2:  sign = "*";
                break;
            case 3:  sign = "/";
                break;
            default: sign = "+";
                break;
        }
        Log.d("sign int","generateSign sign = "+sign);
        return sign;
    }

    /**
     * Random generator of sign for multiplication, dividing
     * 0 = *
     * 1 = /
     * Default *
     * @return sign as string *,/
     */
    private String generatemulti10Sign(){

        Random r = new Random();
        int randomInt = r.nextInt(2);
        Log.d("sign int","generatemulti10Sign int = "+randomInt);
        String sign;
        switch (randomInt) {
            case 0:  sign = "*";
                break;
            case 1:  sign = "/";
                break;
            default: sign = "*";
                break;
        }
        Log.d("sign int","generatemulti10Sign sign = "+sign);
        return sign;
    }

    protected Example(Parcel in) {
        resultStart = in.readInt();
        resultStop = in.readInt();
        numberStart = in.readInt();
        numberStop = in.readInt();
        sign = in.readString();
        extra = in.readString();
        a = in.readInt();
        b = in.readInt();
        result = in.readInt();
        wantedNumber = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resultStart);
        dest.writeInt(resultStop);
        dest.writeInt(numberStart);
        dest.writeInt(numberStop);
        dest.writeString(sign);
        dest.writeString(extra);
        dest.writeInt(a);
        dest.writeInt(b);
        dest.writeInt(result);
        dest.writeInt(wantedNumber);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Example> CREATOR = new Parcelable.Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel in) {
            return new Example(in);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };
}