package com.hellbilling.bambulkacka;

import java.util.Random;

public class Priklad {

    private int start, stop;
    private int a,b,vysledok;
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

}
