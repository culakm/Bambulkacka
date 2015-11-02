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

    private int start;
    private int stop;
    private String znamienko = "+/-";
    private String extra = "nic";
    private int a;
    private int b;
    private int vysledok;


    // Konstruktor bez znamienka
    public Priklad(int parStart, int parStop) {

        this.start = parStart;
        this.stop = parStop;

    }

    // Konstruktor s znamienkom
    public Priklad(int parStart, int parStop, String parZnamienko) {

        this(parStart,parStop);
        this.znamienko = parZnamienko;
    }

    // Konstruktor so znamienkom a s extra
    public Priklad(int parStart, int parStop, String parZnamienko, String parExtra) {

        this(parStart,parStop,parZnamienko);
        this.extra = parExtra;
    }

    public void getCisla() {

        // Generuj nahodne znamienko
        if (znamienko.equals("+/-")) {
            znamienko = generateZnamienko();
        }


        if (znamienko.equals("+")) {
            do {
                if (extra.equals("cez 10")){
                    int aLastDigit = 0;
                    int bLastDigit = 0;
                    do {
                        // Generuj nahodne a
                        a = generateRandomInt();
                        aLastDigit = lastDigit(a);
                    } while (aLastDigit == 0);


                    do {
                        // Generuj nahodne a
                        b = generateRandomInt();
                        bLastDigit = lastDigit(b);
                    } while (aLastDigit + bLastDigit < 10);
                }
                else {
                    a = generateRandomInt();
                    b = generateRandomInt();
                }

                vysledok = a + b;
        Log.d("priklad", "znamienko: " + znamienko + ", a: " + a + ", b: " + b + " = " + vysledok);
        Log.d("podmienka", vysledok + " >=  " + start + " && " + vysledok + " <= " +stop);
        } while ( !(vysledok >= start && vysledok <= stop) );

        }
        else if (znamienko.equals("-")) {
            do {
                b = generateRandomInt();

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
                    // if ( a < b ) ak nastane toto, co je hovadina, je vysledok mensi ako start a ideme este raz
                }

                vysledok = a - b;
                Log.d("priklad",  a + " " + znamienko + " " + b + " = " + vysledok);
                Log.d("podmienka", vysledok + " >=  " + start + " && " + vysledok + " <= " +stop);
                //} while ( !(vysledok >= start && vysledok <= stop)  );
            } while ( (a < b) || (!(vysledok >= start && vysledok <= stop))  );
        }
    }

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

    private static int lastDigit(int number){
        int length = String.valueOf(number).length();
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

    private static int lastDigit(int number, int power){
        int mod = (int) Math.pow(10,power);
        return number % mod;
    }

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

    String getZnamienko (){
        return znamienko;
    }

    String getExtra (){
        return extra;
    }

    String getPrikladString () {  return a + znamienko + b + "="; }

    String getCelyPrikladString () { return getPrikladString() + vysledok; }

    // Random generatory
    private int generateRandomInt(){
        Random r = new Random();
        return r.nextInt(this.stop + 1);
    }

    private boolean generateRandomBool(){
        Random random = new Random();
        return random.nextBoolean();

    }

    private String generateZnamienko(){
        String znamienko;
        if (generateRandomBool()){
            znamienko = "+";
        }
        else {
            znamienko = "-";
        }
        return znamienko;
    }

    protected Priklad(Parcel in) {
        start = in.readInt();
        stop = in.readInt();
        a = in.readInt();
        b = in.readInt();
        vysledok = in.readInt();
        znamienko = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(start);
        dest.writeInt(stop);
        dest.writeString(znamienko);
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