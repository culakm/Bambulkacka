package com.hellbilling.bambulkacka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Calculator as a main part of the project
 */
public class Calculator extends ActionBarActivity implements EditText.OnEditorActionListener{

    /**
     * Database connectivity
     */
    private BambulkackaDB dbh;

    // Nastavenia pre priklady
    /**
     * Range of generated numbers
     */
    private int resultStart, resultStop, numberStart, numberStop;
    /**
     * Sign
     */
    private String exampleSign;
    /**
     * Extra directive
     */
    private String exampleExtra;
    // Other settings
    /**
     * User name
     */
    private String userName;
    /**
     * Number of examples in one exercise
     */
    private int repeat;
    /**
     * Play sound
     */
    private boolean sound;

    // Widgets of activity
    /**
     * Text: Result of example
     */
    private EditText wgResultLocal;
    /**
     * Text: Text of an example withou result
     */
    private TextView wgExampleString;
    /**
     * Text: Error messages and comments for sent result
     */
    private TextView wgErrorString;
    /**
     * Text: Counter of all attempts
     */
    private TextView wgAttemptsCounterString;
    /**
     * Text: Counter of correct attempts
     */
    private TextView wgCorrectCounterString;

    /**
     * Current DB exercise _id
     */
    private long exercise_id;
    /**
     * Current example
     */
    private Example example;
    /**
     * Current DB example _id
     */
    private long example_id;

    /**
     * Text: Counter of all attempts
     */
    private int attempts = 0;
    /**
     * Text: Counter of correct attempts
     */
    private int correct = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // Database connection
        Context ctx = getApplicationContext();
        dbh = new BambulkackaDB(ctx);

        // zistenie widgetov z obrazovky
        wgResultLocal =(EditText)findViewById(R.id.resultString);
        wgExampleString =(TextView)findViewById(R.id.exampleString);
        wgErrorString =(TextView)findViewById(R.id.errorString);
        wgAttemptsCounterString =(TextView)findViewById(R.id.attemptsCounterString);
        wgCorrectCounterString =(TextView)findViewById(R.id.correctCounterString);

        // tu sa nastavil listener onEditorAction na EditText, urcuje ako sa sprava tlacitko go
        wgResultLocal.setOnEditorActionListener(this);

        // Nacitaj settingy
        getSettings();

        // riesi zotavenie po zmene orientacie
        restoreMe(savedInstanceState);

        // Save exercise
        exercise_id = dbh.insertExerciseStart(exampleSign,Utils.getNow());

        // nacitame aktualny example ak este neexistuje, inak sa taha z restoreMe
        if(example ==null) {
            getExample();
        }

        // otvorim klavesnicu
        showSoftKeyboard();
    }

    /**
     * Load new example
     */
    private void getExample() {

        example = new Example(resultStart, resultStop, numberStart, numberStop, exampleSign, exampleExtra);

        // Check example duplicity in exercise
        do {
            example.getNumbers();
        } while (checkRepeat(exercise_id, example));

        example_id = dbh.insertExample(exercise_id, example.getA(), example.getB(), example.getSign(), example.getResult());
        // Set up text of the example
        wgExampleString.setText(example.getExampleString());
    }

    /**
     * Check if this example has been already generated for this exercise
     * we don't want repeat the same example
     */

    private boolean checkRepeat(long exercise_id, Example example){

        Context ctx = this;
        BambulkackaDB dbh = new BambulkackaDB(ctx);
        Cursor examplesCursor = dbh.getResultExamples(String.valueOf(exercise_id));

        // Current example
        int priklad_a = example.getA();
        int priklad_b = example.getB();
        // if Cursor is contains results
        if (examplesCursor != null) {
            // move cursor to first row
            if (examplesCursor.moveToFirst()) {
                do {
                    // Database example
                    String a = examplesCursor.getString(examplesCursor.getColumnIndex("a"));
                    String b = examplesCursor.getString(examplesCursor.getColumnIndex("b"));
                    if (Integer.parseInt(a) == priklad_a && Integer.parseInt(b) == priklad_b){
                        return true;
                    }
                } while (examplesCursor.moveToNext());
            }
        }
        dbh.close();
        return false;
    }

    /**
     * Check input value of result and evaluate it
     */
    private void manageResult() {

        String resultLocalStr = wgResultLocal.getText().toString();
        int vysledokLocalInt;

        wgErrorString.setTextColor(getResources().getColor(R.color.default_color));
        wgErrorString.setText("");

        // vysledok je prazdny
        if (resultLocalStr.length() == 0){
            //vysledokLocalInt = 0;
            wgErrorString.setText(userName + " , " + getResources().getString(R.string.empty_result) + ".");
            return;
        }

        // vysledok nie je cislo
        if ( ! resultLocalStr.matches("\\d+")) {
            //vysledokLocalInt = 0;
            wgErrorString.setText(userName + ", " + getResources().getString(R.string.natural_numbers) + "!");
            wgResultLocal.setText("");
            return;
        }
        else {
            try {
                vysledokLocalInt = Integer.parseInt(resultLocalStr);
            } catch(NumberFormatException nfe) {
                wgErrorString.setText(getResources().getString(R.string.num_limit) + ": \n" + nfe);
                wgResultLocal.setText("");
                return;
            }
        }

        // Vysledok dobre
        if (vysledokLocalInt == example.getResult()){
            Log.d("++", "vysledok je dobre");
            // prehrajeme ok zvuk
            if (sound){playSound("ok");}

            wgCorrectCounterString.setText(++correct + "");
            wgAttemptsCounterString.setText(++attempts + "");
            wgErrorString.setTextColor(getResources().getColor(R.color.spravne_color));
            wgErrorString.setText(userName + ", " + getResources().getString(R.string.genius) + ", " + example.getExampleStringFull() + ", ides dalej.");

            // Save attempt
            dbh.insertAttempt(example_id, vysledokLocalInt, Utils.getNow(), 1);

            // ak je dokoncene tak otvori resume aktivitu
            if (correct == repeat){
                Intent intent;
                intent = new Intent(this, ResumeActivity.class);
                intent.putExtra("correct", correct);
                intent.putExtra("attempts", attempts);

                // Save exercise end
                dbh.updateExerciseEnd(exercise_id,Utils.getNow());

                startActivity(intent);
            } // inak da novy example
            else {
                getExample();
            }
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok " + vysledokLocalInt + " je zle");
            // Prehrajeme NOK zvuk
            if (sound){playSound("nok");}
            wgAttemptsCounterString.setText(++attempts + "");
            //wgErrorString.setTextColor(getResources().getIdentifier("nespravne_color", "color", getPackageName()));
            wgErrorString.setTextColor(getResources().getColor(R.color.nespravne_color));
            wgErrorString.setText(userName + ", " + userName + ". " + getResources().getString(R.string.compl_wrong) + "!");

            // Save attempt
            dbh.insertAttempt(example_id, vysledokLocalInt, Utils.getNow(), 0);
        }

        // Vynuluj editText
        wgResultLocal.setText("");

    }

    /**
     * Load preferences
     */
    private void getSettings(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        resultStart = Integer.parseInt(sharedPref.getString("result_start", "0"));
        resultStop = Integer.parseInt(sharedPref.getString("result_stop", "100"));
        numberStart = Integer.parseInt(sharedPref.getString("number_start", "0"));
        numberStop = Integer.parseInt(sharedPref.getString("number_stop", "100"));
        repeat = Integer.parseInt(sharedPref.getString("repeat", "10"));
        exampleSign = sharedPref.getString("sign", "all");
        exampleExtra = sharedPref.getString("extra", "nic");
        userName = sharedPref.getString("user_name", "Detisko");
        sound = sharedPref.getBoolean("sound", true);
        //Toast.makeText(getApplicationContext(), "start: " + resultStart + ", stop: " + resultStop + ", sign: " + exampleSign + ", extra: " + exampleExtra + ", username: " + userName, Toast.LENGTH_SHORT).show();
        //Log.d( "settings: " , "start: " + resultStart + ", stop: " + resultStop + ", sign: " + exampleSign + ", extra: " + exampleExtra + ", username: " + userName);
    }

    /**
     * Submit button handling
     * <p>
     * This method is assigned in activity_calculator.xml
     * @param view view no pokec
     */
    public void submitResult(View view) {
        manageResult();
    }

    /**
     * Keyboard handling
     */
    // Toto moze byt divne, ako vieme ku ktoremu editTextu to otvara tu klavesnicu?
    void showSoftKeyboard() {
        // otvorenie klavesnice
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // zatvorenie klavesnice
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
// toto mi nefungovalo
//        if (view.requestFocus()) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
//            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//        }
    }

    /**
     * Listener for submit button, action GO
     * @param view view bez pokecu
     * @param actionId GO,or something else?
     * @param event ???
     * @return true if action is GO
     */
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_GO) {
            manageResult();
            handled = true;
        }
        return handled;
    }

    /**
     * Play sound
     * @param filename source of sound
     */
    private void playSound (String filename) {
        // Find file as resource
        int res = getResources().getIdentifier(filename, "raw", getPackageName());
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), res);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
    }

    //// Obsluha zapamatania
    // ukladame premenne
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("example", example);
        outState.putInt("attempts", attempts);
        outState.putInt("correct", correct);
    }

    // Obnovujeme zapamatany stav
    //je zaujimave ze wgResultLocal ma hodnotu zachovanu bez toho ze by sme to riesili
    private void restoreMe(Bundle state){

        if  (state != null){

            example = state.getParcelable("example");
            attempts = state.getInt("attempts");
            correct = state.getInt("correct");
            // Nastavi text prikladu
            wgExampleString.setText(example.getExampleString());
            wgCorrectCounterString.setText(correct + "");
            wgAttemptsCounterString.setText(attempts + "");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Startovanie settings aktivity
            case R.id.settings:
                Intent intent;
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("preferencesType","calculator");
                startActivity(intent);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.d("myTag", "This is onOptionsMenuClosed");
        getExample();
    }

    @Override
    public void onBackPressed(){
        goHome();
    }

    private void goHome(){
        // Save exercise end
        dbh.updateExerciseEnd(exercise_id,Utils.getNow());
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
