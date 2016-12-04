package com.hellbilling.bambulkacka;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

// Parcelable je implemetnovane aby sa objekt priklad dal prehadzovat cez onSaveInstanceState
// parcerable som ziskal hodenim kodu do http://www.parcelabler.com/
/**
 * Generates example for calculator
 */
public class Example implements Parcelable {

    /**
     * Range of generated numbers
     */
    private final int resultStart;
    /**
     * Range of generated numbers
     */
    private final int resultStop;
    /**
     * Range of generated numbers
     */
    private final int numberStart;
    /**
     * Range of generated numbers
     */
    private final int numberStop;
    /**
     * Signs delimited by commas
     */
    private String signsString = "+";
    /**
     * Sings
     */
    private ArrayList<String> signs;
    /**
     * Sign used in the example
     */
    private String sign = "+";

    /**
     * extra request
     */
    private String extra = "";
    private int a;
    private int b;
    private int result;
    private int wantedNumber;

    /**
     * Constructor with ExampleSetting class
     */
    public Example(ExampleSetting exampleSetting) {

        this.resultStart = exampleSetting.resultStart;
        this.resultStop = exampleSetting.resultStop;
        this.numberStart = exampleSetting.numberStart;
        this.numberStop = exampleSetting.numberStop;
        this.signsString = exampleSetting.sign;
        this.signs = signToArrayList(signsString);
        this.extra = exampleSetting.extra;

    }


    /**
     * SUBNUMBERS STOP
     */

    /**
     * Load all values into Example
     */
    public void getExample() {
        // Load random sign from signs
        sign = Utils.generateRandomSign(signs);
        int[] numbers;
        switch (extra) {
            //This case uses default
            //case "10+-":
            case "20notOver10":
                numbers = get20notOver10();
                a = numbers[0];
                b = numbers[1];
                break;
            case "20Over10":
                numbers = get20Over10();
                a = numbers[0];
                b = numbers[1];
                break;
            case "100notOver10":
                numbers = get100notOver10();
                a = numbers[0];
                b = numbers[1];
                break;
            case "100with1Over10":
                numbers = get100with1Over10();
                a = numbers[0];
                b = numbers[1];
                break;
            case "100with10oneWhole10":
                numbers = get100with10oneWhole10();
                a = numbers[0];
                b = numbers[1];
                break;
            case "100with10notOver10":
                numbers = get100with10notOver10();
                a = numbers[0];
                b = numbers[1];
                break;
            default:
                // Default regular examples
                if (sign.equals("+")) {
                    numbers = getPlusNumbers();
                    a = numbers[0];
                    b = numbers[1];
                }
                if (sign.equals("-")) {
                    numbers = getMinusNumbers();
                    a = numbers[0];
                    b = numbers[1];
                }
                if (sign.equals("*")) {
                    numbers = getMultipleNumbers();
                    a = numbers[0];
                    b = numbers[1];
                }
                if (sign.equals("/")) {
                    numbers = getDivisionNumbers();
                    a = numbers[0];
                    b = numbers[1];
                }
        }


//a osetrit vyjimku pri nevhodne zadanych cislach v preferenciach

        //http://www.objecthunter.net/exp4j/index.html
        Expression e = new ExpressionBuilder("a " + sign + " b")
                .variables("a", "b")
                .build()
                .setVariable("a", a)
                .setVariable("b", b);
        result = (int) e.evaluate();

        Log.d("ex-podmienka", "extra : " + extra);
        Log.d("ex-priklad", a + " " + sign + " " + b + " = " + result);
        Log.d("ex-podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);

    }

    /**
     * Extra math fuction
     * 1 :
     */
    private int[] get100with10notOver10() {
        // Count of subtypes
        int countSubTypes = 1;
        // Random sub type
        int subType = Utils.generateRandomInt(1, countSubTypes);
        int a = 0;
        int b = 0;
        int part = 0;
        Log.d("ex-podmienka", "get100with1Over10 subType : " + subType);
        switch (subType) {
            // cela desiatka + hocijaka desiatka 30+62
            case 1:
                if (sign.equals("+")) {
                    // last digit of a
                    a = Utils.generateRandomInt(10, 90);
                    // 90 needs 10 only
                    if (a == 90) {
                        b = 10;
                    } else {
                        int la = Utils.lastDigit(a);
                        int aux_a = a;
                        // 0 at the end needs decimal part lower by 1
                        if (la == 0) {
                            aux_a = a + 10;
                        }
                        //cislo je zlozene:                           desiatkova cast cisla + jednotkova cast cisla
                        b = Utils.generateRandomIntWhole10(10, (((100 - aux_a) / 10) * 10)) + Utils.generateRandomInt(0, 9 - la);
                    }
                } // -
                else {
                    a = Utils.generateRandomInt(20, 99);
                    if (a == 20) {
                        b = 10;
                    }
                    else {
                        int la = Utils.lastDigit(a);
                        b = Utils.generateRandomIntWhole10(10, a - la) + Utils.generateRandomInt(0, la);
                    }
                }
                break;
        }
        int array [] = {a,b};
        return array;
    }

    /**
     * Extra math fuction
     * 1 : cela desiatka + hocijaka desiatka 30+62
     * 2 : hocijaka desiatka - cela desiatka, 62-30
     * 3: hocijaka desiatka - hocijaka desiatka = cela desiatka, 62-32
     */
    private int[] get100with10oneWhole10() {
        // Count of subtypes
        int countSubTypes = 3;
        // Random sub type
        int subType = Utils.generateRandomInt(1, countSubTypes);
        int a = 0;
        int b = 0;
        int part = 0;
        Log.d("ex-podmienka", "get100with1Over10 subType : " + subType);
        switch (subType) {
            // cela desiatka + hocijaka desiatka 30+62
            case 1:
                a = Utils.generateRandomIntWhole10(10, 90);
                b = Utils.generateRandomInt(10, 100 - a);
                sign = "+";
                break;
            // hocijaka desiatka - cela desiatka, 62-30
            case 2:
                a = Utils.generateRandomInt(10, 99);
                b = Utils.generateRandomIntWhole10(10, a - Utils.lastDigit(a));
                sign = "-";
                break;
            // hocijaka desiatka - hocijaka desiatka = cela desiatka, 62-32
            case 3:
                a = Utils.generateRandomInt(10, 99);
                int la = Utils.lastDigit(a);
                b = Utils.generateRandomIntWhole10(10, a - la);
                b += la;
                sign = "-";
                break;
        }
        int array [] = {a,b};
        return array;
    }

    /**
     * Extra math fuction
     * 1 : desiatky +- jednotky s prechodom cez desat, 43+9, 52-5
     */
    private int[] get100with1Over10() {
        // Count of subtypes
        int countSubTypes = 1;
        // Random sub type
        int subType = Utils.generateRandomInt(1, countSubTypes);
        int a = 0;
        int b = 0;
        int part = 0;
        Log.d("ex-podmienka", "get100with1Over10 subType : " + subType);
        switch (subType) {
            // desiatky +- jednotky s prechodom cez desat, 43+9, 52-5
            case 1:
                if (sign.equals("+")) {
                    // last digit of a
                    int la = 0;
                    do {
                        a = Utils.generateRandomInt(11, 89);
                        la = Utils.lastDigit(a);
                    }
                    while (la == 0); // We don't want 0
                    b = Utils.generateRandomInt(10 - la, 9);
                } // -
                else {
                    // last digit of a
                    int la = 0;
                    do {
                        a = Utils.generateRandomInt(11, 88);
                        la = Utils.lastDigit(a);
                    }
                    while (la == 9); // We don't want 9
                    b = Utils.generateRandomInt(la + 1, 9);
                }
                break;
        }
        int array [] = {a,b};
        return array;
    }


    /**
     * Extra math fuction
     * 1 : +/-cele desiatky, 40+20, 50-10
     * 2: +/- cele desiatky, jednotky 50 +/- 6
     * 3: desiatka - jednotka, 25 - 5
     * 4: jednotky + desiatky bez prechodu cez 10, 5 + 24
     * 5: desiatky - jednotky bez prechodu cez 10, 28 - 5
     * 6: desiatky - jednotky = cela desiatka, 28 + 2
     * 7: // cela desiatka - jednotky, 20 - 6
     */
    private int[] get100notOver10() {
        // Count of subtypes
        int countSubTypes = 7;
        // Random sub type
        int subType = Utils.generateRandomInt(1, countSubTypes);
        int a = 0;
        int b = 0;
        int part = 0;

        Log.d("ex-podmienka", "get20noOver10 subType : " + subType);
        switch (subType) {
            // +/-cele desiatky, 40+20, 50-10
            case 1:
                if (sign.equals("+")) {
                    a = Utils.generateRandomIntWhole10(10, 90);
                    b = Utils.generateRandomIntWhole10(10, 100 - a);
                } // -
                else {
                    a = Utils.generateRandomIntWhole10(10, 100);
                    b = Utils.generateRandomIntWhole10(10, a);
                }
                break;
            // +/- cele desiatky, jednotky 50 +/- 6
            case 2:
                if (sign.equals("+")) {
                    a = Utils.generateRandomIntWhole10(10, 90);
                } // -
                else {
                    a = Utils.generateRandomIntWhole10(10, 100);
                }
                b = Utils.generateRandomInt(1, 9);
                break;
            // desiatka - jednotka, 25 - 5
            case 3:
                a = Utils.generateRandomInt(10, 99);
                b = Utils.lastDigit(a);
                sign = "-";
                break;
            // jednotky + desiatky bez prechodu cez 10, 5 + 24
            case 4:
                a = Utils.generateRandomInt(1,9);
                int b1 = Utils.generateRandomIntWhole10(10, 90);
                int b2 = Utils.generateRandomInt(1, 10 - a);
                b = b1 + b2;
                sign = "+";
                break;
            // desiatky - jednotky bez prechodu cez 10, 28 - 5
            case 5:
                a = Utils.generateRandomInt(11,99);
                b = Utils.generateRandomInt(0,Utils.lastDigit(a));
                sign = "-";
                break;
            // desiatky - jednotky = cela desiatka, 28 + 2
            case 6:
                a = Utils.generateRandomInt(11,99);
                b = 10 - Utils.lastDigit(a);
                sign = "+";
                break;
            // cela desiatka - jednotky, 20 - 6
            case 7:
                a = Utils.generateRandomIntWhole10(10, 90);
                b = Utils.generateRandomInt(1,9);
                sign = "-";
                break;
        }
        int array [] = {a,b};
        return array;
    }


    /**
     * Extra math fuction
     * 1..9 + 1..9 > 10
     * 1..10 + 10..20
     * 11 .. 20 - 11 .. 20
     * 11 .. 20 - 2 .. 9, lastDigit(a) < b
     */
    private int[] get20Over10() {
        // Count of subtypes
        int countSubTypes = 3;
        // Random sub type
        int subType = Utils.generateRandomInt(1, countSubTypes);
        int a = 0;
        int b = 0;
        int part = 0;
        Log.d("ex-podmienka", "get20Over10 subType : " + subType);
        switch (subType) {
            // 1..9 + 1..9 > 10
            case 1:
                a = Utils.generateRandomInt(1, 9);
                b = Utils.generateRandomInt(10 - (a - 1), 10);
                sign = "+";
                break;
            // 1..10 + 10..20
            case 2:
                a = Utils.generateRandomInt(1, 9);
                b = Utils.generateRandomInt(10, 20 - a);
                sign = "+";
                break;
            // 11 .. 20 - 11 .. 20
            case 3:
                a = Utils.generateRandomInt(11, 20);
                part = Utils.lastDigit(a);
                b = Utils.generateRandomInt(11, 20);
                if (b > a){
                    int c = a;
                    a = b;
                    b = c;
                }
                sign = "-";
                break;
            // 11 .. 20 - 2 .. 9, lastDigit(a) < b
            case 4:
                a = Utils.generateRandomInt(11, 20);
                part = Utils.lastDigit(a);
                b = Utils.generateRandomInt(part + 1, 10);
                sign = "-";
                break;
        }
        int array [] = {a,b};
        return array;
    }

        /**
         * Extra math fuction
         * 10 + 6 - start with 10
         * 16 - 6 = 10
         * 15 - 10 = 5
         * 13 + 5 = 10
         * 18 - 3 = 15
         */
    private int[] get20notOver10() {

        // Count of subtypes
        int countSubTypes = 5;
        // Random sub type
        int subType = Utils.generateRandomInt(1, countSubTypes);
        int a = 0;
        int b = 0;
        int part = 0;
        Log.d("ex-podmienka", "get20noOver10 subType : " + subType);
        switch (subType) {
            // 10 + 6 - start with 10
            case 1:
                a = 10;
                b = Utils.generateRandomInt(1, 10);
                sign = "+";
                break;
            // 16 - 6 = 10
            case 2:
                a = Utils.generateRandomInt(10, 20);
                b = Utils.lastDigit(a);
                sign = "-";
                break;
            // 15 - 10 = 5
            case 3:
                a = Utils.generateRandomInt(10, 20);
                b = 10;
                sign = "-";
                break;
            // 13 + 5 = 10
            case 4:
                a = Utils.generateRandomInt(10, 20);
                part = Utils.lastDigit(a);
                b = Utils.generateRandomInt(1, 10 - part);
                sign = "+";
                break;
            // 18 - 3 = 15
            case 5:
                a = Utils.generateRandomInt(10, 20);
                part = Utils.lastDigit(a);
                b = Utils.generateRandomInt(0, part);
                sign = "-";
                break;
        }
        int array [] = {a,b};
        return array;
    }
    /**
     * Basic math functions
     */
    private int[] getPlusNumbers() {
        // Random a
        int a = Utils.generateRandomInt(numberStart, numberStop);
        // Random b
        int b = Utils.generateRandomInt(numberStart, resultStop - a);
        int array [] = {a,b};
        return array;
    }
    private int[] getMinusNumbers() {
        // Random a
        int a = Utils.generateRandomInt(numberStart, numberStop);
        // Random b
        int b =  Utils.generateRandomInt(numberStart, a);
        int array [] = {a,b};
        return array;
    }
    private int[] getMultipleNumbers() {
        // Random a
        int a = Utils.generateRandomInt(numberStart, numberStop);
        // Random b
        int b =  Utils.generateRandomInt(numberStart, resultStop / a);
        int array [] = {a,b};
        return array;
    }
    private int[] getDivisionNumbers() {
        int numberStartLocal = numberStart;
        // Can't use 0
        if(numberStartLocal == 0){numberStartLocal = 1;}
        // Use multiple first
        // Random a
        int a = Utils.generateRandomInt(numberStart, numberStop);
        // Random b
        int b =  Utils.generateRandomInt(numberStart, resultStop / a);
        int result = a * b;
        a = result;
        int array [] = {a,b};
        return array;
    }

    /**
     * Convert string of signs delimited by comas into ArrayList
     * @param sign signs string "+,-.*,..."
     * @return
     */
    public static ArrayList signToArrayList (String sign){
        //List<String> myList = new ArrayList<String>(Arrays.asList(s.split(",")));
        ArrayList<String> signs = new ArrayList<String>(Arrays.asList(sign.split(",")));
        return signs;
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

    String getSignsString(){
        return signsString;
    }

    String getExtra (){
        return extra;
    }

    String getExampleString() {  return a + sign + b + "="; }

    String getExampleStringFull() { return getExampleString() + result; }


    /** Parcel part
     *
     * @param in
     */
    protected Example(Parcel in) {
        resultStart = in.readInt();
        resultStop = in.readInt();
        numberStart = in.readInt();
        numberStop = in.readInt();
        signsString = in.readString();
        if (in.readByte() == 0x01) {
            signs = new ArrayList<String>();
            in.readList(signs, String.class.getClassLoader());
        } else {
            signs = null;
        }
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
        dest.writeString(signsString);
        if (signs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(signs);
        }
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

/*
        // + cast
        if (signsString.equals("+")) {
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
                Log.d("priklad", "signsString: " + signsString + ", a: " + a + ", b: " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
            } while ( !(result >= resultStart && result <= resultStop) );

        } // - cast
        else if (signsString.equals("-")) {
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
                Log.d("priklad",  a + " " + signsString + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( (a < b) || (!(result >= resultStart && result <= resultStop))  );
        } // * cast
        else if (signsString.equals("*")) {
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
                Log.d("priklad",  a + " " + signsString + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( !(result >= resultStart && result <= resultStop)  );
        }
        else if (signsString.equals("/")) {
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
                Log.d("priklad",  a + " " + signsString + " " + b + " = " + result);
                Log.d("podmienka", result + " >=  " + resultStart + " && " + result + " <= " + resultStop);
                //} while ( !(result >= resultStart && result <= resultStop)  );
            } while ( !(result >= resultStart && result <= resultStop)  );
        }
*/