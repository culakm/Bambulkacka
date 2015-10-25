package com.hellbilling.bambulkacka;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

// Parcelable je implemetnovane aby sa objekt priklad dal prehadzovat cez onSaveInstanceState
// nparcerable som ziskal hodenim kodu do http://www.parcelabler.com/
public class Priklad implements Parcelable {

    private int start;
    private int stop;
    private int a;
    private int b;
    private int vysledok;
    private String znamienko;

    // Konstruktor bez znamienka
    public Priklad(int parStart, int parStop) {

        this.start = parStart;
        this.stop = parStop;

        if (generateRandomBool()){
            this.znamienko = "+";
        }
        else {
            this.znamienko = "-";
        }

    }

    // Konstruktor so znamienkom
    public Priklad(int parStart, int parStop, String parZnamienko) {
        this(parStart,parStop);
        this.znamienko = parZnamienko;
    }

    public void getCisla(){

        a = generateRandomInt();

        if (znamienko.equals("+")){

            do {
                b = generateRandomInt();
                vysledok = a + b;
            } while ( !(vysledok >= start && vysledok <= stop) );

        }
        else {

            do {
                b = generateRandomInt();
                vysledok = a - b;
            } while ( !(vysledok >= start && vysledok <= stop) );
        }
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


    protected Priklad(Parcel in) {
        start = in.readInt();
        stop = in.readInt();
        a = in.readInt();
        b = in.readInt();
        vysledok = in.readInt();
        znamienko = in.readString();
    }

    // Odtialto to tam doplnilo parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(start);
        dest.writeInt(stop);
        dest.writeInt(a);
        dest.writeInt(b);
        dest.writeInt(vysledok);
        dest.writeString(znamienko);
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