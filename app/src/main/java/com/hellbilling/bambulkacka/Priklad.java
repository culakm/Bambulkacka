package com.hellbilling.bambulkacka;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Parcelable je implemetnovane aby sa objekt priklad dal prehadzovat cez onSaveInstanceState
// nparcerable som ziskal hodenim kodu do http://www.parcelabler.com/
public class Priklad implements Parcelable {

    private int resultStart, resultStop, numberStart, numberStop;
    private String sign = "all";
    private String extra = "nic";
    private int a;
    private int b;
    private int vysledok;


    // Konstruktor bez znamienka
    public Priklad(int parResultStart, int parResultStop, int parNumberStart, int parNumberStop) {

        this.resultStart = parResultStart;
        this.resultStop = parResultStop;
        this.numberStart = parNumberStart;
        this.numberStop = parNumberStop;

    }

    // Konstruktor s znamienkom
    public Priklad(int parResultStart, int parResultStop, int parNumberStart, int parNumberStop, String parSign) {

        this(parResultStart,parResultStop,parNumberStart,parNumberStop);
        this.sign = parSign;
    }

    // Konstruktor so znamienkom a s extra
    public Priklad(int parResultStart, int parResultStop, int parNumberStart, int parNumberStop, String parSign, String parExtra) {

        this(parResultStart,parResultStop,parNumberStart,parNumberStop,parSign);
        this.extra = parExtra;
    }

    public void getCisla() {

        // Generuj nahodne sign
        if (sign.equals("all")) {
            sign = generateSign();
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
                        //Log.d("+ cez10", "a: " + a + ", aLastDigit: " + aLastDigit);
                        if (lengthDigit(a) != lengthDigit(b)){
                            int minLength = Math.min(lengthDigit(a), lengthDigit(b));
                            power = minLength - 1;
                            aLastDigit = lastDigit(a,power);
                            //Log.d("+ cez10", "aLastDigit: " + aLastDigit);
                            bLastDigit = lastDigit(b,power);
                            //Log.d("+ cez10", "bLastDigit: " + bLastDigit);
                        }
                        else {
                            power = lengthDigit(a) - 1;
                            bLastDigit = lastDigit(b);
                        }

                        //Log.d("+ cez10", "b: " + b + ", bLastDigit: " + bLastDigit);
                        // sucet lastDigitov musi byt vacsi ako 10^power
                        //Log.d("+ cez10", "sucet latdigitov: " + (aLastDigit + bLastDigit) + ", power: " + power +" < 10 power " + (Math.pow(10,power)) );
                    } while (aLastDigit + bLastDigit < Math.pow(10,power));
                } // normal
                else {
                    a = generateRandomNumber();
                    b = generateRandomNumber();
                }

                vysledok = a + b;
                Log.d("priklad", "sign: " + sign + ", a: " + a + ", b: " + b + " = " + vysledok);
                Log.d("podmienka", vysledok + " >=  " + resultStart + " && " + vysledok + " <= " + resultStop);
            } while ( !(vysledok >= resultStart && vysledok <= resultStop) );

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
                    //Log.d("cez 10 pred",  a + " = " +  b);
                    int aLastDigit = lastDigit(a);
                    int bLastDigit = lastDigit(b);

                    if (aLastDigit > bLastDigit){
                        a = changeDigit(a,bLastDigit);
                        b = changeDigit(b,aLastDigit);
                    }
                    //Log.d("cez 10 po",  a + " = " +  b);
                    // if ( a < b ) ak nastane toto, co je hovadina, je vysledok mensi ako resultStart a ideme este raz
                }
                vysledok = a - b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + vysledok);
                Log.d("podmienka", vysledok + " >=  " + resultStart + " && " + vysledok + " <= " + resultStop);
                //} while ( !(vysledok >= resultStart && vysledok <= resultStop)  );
            } while ( (a < b) || (!(vysledok >= resultStart && vysledok <= resultStop))  );
        } // * cast
        else if (sign.equals("*")) {
            do {
                b = generateRandomNumber();

                vysledok = a * b;
                Log.d("priklad",  a + " " + sign + " " + b + " = " + vysledok);
                Log.d("podmienka", vysledok + " >=  " + resultStart + " && " + vysledok + " <= " + resultStop);
                //} while ( !(vysledok >= resultStart && vysledok <= resultStop)  );
            } while ( !(vysledok >= resultStart && vysledok <= resultStop)  );
        }
    }

    // vymeni posledne cislice number za lastDigit
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

    // zisti dlzku number
    private static int lengthDigit(int number){
        return String.valueOf(number).length();
    }

    // zisti posledne cislice od prvej cislice number
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
    private static int lastDigit(int number, int power){
        int mod = (int) Math.pow(10,power);
        return number % mod;
    }

    // vytvori pole vsetkych cislic v num
    private List<Integer> splitNumer (int num){

        List<Integer> list = new ArrayList<>();
        String numStr = Integer.toString(num);

        for(int i=0;i<numStr.length();i++)
        {
            list.add(Character.getNumericValue(numStr.charAt(i)));
        }
        return list;
    }

    // Gety
    int getA (){
        return a;
    }

    int getB (){
        return b;
    }

    int getVysledok (){
        return vysledok;
    }

    String getSign(){
        return sign;
    }

    String getExtra (){
        return extra;
    }

    String getPrikladString () {  return a + sign + b + "="; }

    String getCelyPrikladString () { return getPrikladString() + vysledok; }

    // Random generatory
    private int generateRandomNumber(){
        return generateRandomInt(this.numberStart,this.numberStop);
    }
    // Random generatory
    private int generateRandomInt(int parResultStart,int parResultStop){
        Random r = new Random();
        return r.nextInt(parResultStop - parResultStart + 1) + parResultStart;
    }


    private boolean generateRandomBool(){
        Random random = new Random();
        return random.nextBoolean();

    }

    private String generateSign(){

        Random r = new Random();
        int randomInt = r.nextInt(3);
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
        Log.d("SIGN", sign);
        return sign;



    }

    protected Priklad(Parcel in) {
        resultStart = in.readInt();
        resultStop = in.readInt();
        numberStart = in.readInt();
        numberStop = in.readInt();
        a = in.readInt();
        b = in.readInt();
        vysledok = in.readInt();
        sign = in.readString();
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
        dest.writeInt(vysledok);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Priklad> CREATOR = new Parcelable.Creator<Priklad>() {
        @Override
        public Priklad createFromParcel(Parcel in) {
            return new Priklad(in);
        }

        @Override
        public Priklad[] newArray(int size) {
            return new Priklad[size];
        }
    };
}