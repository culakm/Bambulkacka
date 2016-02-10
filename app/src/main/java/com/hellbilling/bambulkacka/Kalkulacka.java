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

public class Kalkulacka extends ActionBarActivity implements EditText.OnEditorActionListener{

    // Database connectivity
    private BambulkackaDB dbh;

    // Nastavenia pre priklady
    // rozsah generovanych cisel
    private int resultStart, resultStop, numberStart, numberStop;
    // Sign
    private String exampleSign;
    // Extra
    private String exampleExtra;
    // Ine nastavenie
    private String userName;
    // pocet prikladov
    private int repeat;
    // Play sound ?
    private boolean sound;

    // Widgety aktivity
    // Tu zadavame vysledok
    private EditText vysledokLocal;
    // text zadania prikladu
    private TextView prikladText;
    // Chybove hlasenia, a komentare k odoslanemu vysledku
    private TextView errorText;
    // Zobrazovanie poctu vsetkych pokusov
    private TextView textPokusovCounter;
    // Zobrazovanie poctu spravnych pokusov
    private TextView textSpravneCounter;

    // Current exercise _id
    private long exercise_id;
    // Current example
    private Priklad priklad;
    // Current example _id
    private long example_id;

    // Pocitadla
    private int pokusov = 0;
    private int spravne = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulacka);

        // Database connection
        Context ctx = getApplicationContext();
        dbh = new BambulkackaDB(ctx);

        // zistenie widgetov z obrazovky
        vysledokLocal =(EditText)findViewById(R.id.vysledok_text);
        prikladText =(TextView)findViewById(R.id.prikladText);
        errorText =(TextView)findViewById(R.id.errorText);
        textPokusovCounter =(TextView)findViewById(R.id.textPokusovCounter);
        textSpravneCounter =(TextView)findViewById(R.id.textSpravneCounter);

        // tu sa nastavil listener onEditorAction na EditText, urcuje ako sa sprava tlacitko go
        vysledokLocal.setOnEditorActionListener(this);

        // Nacitaj settingy
        getSettings();

        // riesi zotavenie po zmene orientacie
        restoreMe(savedInstanceState);

        // Save exercise
        exercise_id = dbh.insertExerciseStart(exampleSign,Utils.getNow());

        // nacitame aktualny priklad ak este neexistuje, inak sa taha z restoreMe
        if(priklad==null) {
            getPriklad();
        }

        // otvorim klavesnicu
        showSoftKeyboard();
    }

    // Nacita priklad
    private void getPriklad() {

        priklad = new Priklad(resultStart, resultStop, numberStart, numberStop, exampleSign, exampleExtra);

        // Check example duplicity in exercise
        do {
            priklad.getNumbers();
        } while (checkRepeat(exercise_id,priklad));

        example_id = dbh.insertExample(exercise_id, priklad.getA(), priklad.getB(), priklad.getSign(), priklad.getResult());
        // Set up text of the example
        prikladText.setText(priklad.getPrikladString());
    }

    // Check if Priklad has been already generated
    private boolean checkRepeat(long exercise_id, Priklad priklad){

        Context ctx = this;
        BambulkackaDB dbh = new BambulkackaDB(ctx);
        Cursor examplesCursor = dbh.getResultExamples(String.valueOf(exercise_id));

        // Current example
        int priklad_a = priklad.getA();
        int priklad_b = priklad.getB();
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

    // spracujeme odoslany vysledok
    private void zpracujVysledok () {

        String vysledokLocalStr = vysledokLocal.getText().toString();
        int vysledokLocalInt;

        errorText.setTextColor(getResources().getColor(R.color.default_color));
        errorText.setText("");

        // vysledok je prazdny
        if (vysledokLocalStr.length() == 0){
            //vysledokLocalInt = 0;
            errorText.setText(userName + " , " + getResources().getString(R.string.empty_result) + ".");
            return;
        }

        // vysledok nie je cislo
        if ( ! vysledokLocalStr.matches("\\d+")) {
            //vysledokLocalInt = 0;
            errorText.setText(userName + ", " + getResources().getString(R.string.natural_numbers) + "!");
            vysledokLocal.setText("");
            return;
        }
        else {
            try {
                vysledokLocalInt = Integer.parseInt(vysledokLocalStr);
            } catch(NumberFormatException nfe) {
                errorText.setText(getResources().getString(R.string.num_limit) + ": \n" + nfe);
                vysledokLocal.setText("");
                return;
            }
        }

        // Vysledok dobre
        if (vysledokLocalInt == priklad.getResult()){
            Log.d("++", "vysledok je dobre");
            // prehrajeme ok zvuk
            if (sound){playSound("ok");}

            textSpravneCounter.setText(++spravne + "");
            textPokusovCounter.setText(++pokusov + "");
            errorText.setTextColor(getResources().getColor(R.color.spravne_color));
            errorText.setText(userName + ", " + getResources().getString(R.string.genius) +", " + priklad.getCelyPrikladString() + ", ides dalej.");

            // Save attempt
            dbh.insertAttempt(example_id, vysledokLocalInt, Utils.getNow(), 1);

            // ak je dokoncene tak otvori resume aktivitu
            if (spravne == repeat){
                Intent intent;
                intent = new Intent(this, ResumeActivity.class);
                intent.putExtra("spravne",spravne);
                intent.putExtra("pokusov",pokusov);

                // Save exercise end
                dbh.updateExerciseEnd(exercise_id,Utils.getNow());

                startActivity(intent);
            } // inak da novy priklad
            else {
                getPriklad();
            }
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok " + vysledokLocalInt + " je zle");
            // Prehrajeme NOK zvuk
            if (sound){playSound("nok");}
            textPokusovCounter.setText(++pokusov + "");
            //errorText.setTextColor(getResources().getIdentifier("nespravne_color", "color", getPackageName()));
            errorText.setTextColor(getResources().getColor(R.color.nespravne_color));
            errorText.setText(userName + ", " + userName + ". " + getResources().getString(R.string.compl_wrong) + "!");

            // Save attempt
            dbh.insertAttempt(example_id, vysledokLocalInt, Utils.getNow(), 0);
        }

        // Vynuluj editText
        vysledokLocal.setText("");

    }

    // Load preferences
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

    //// Obsluha odosielacieho tlacitka, toto je mu priradene v activity_kalkulacka.xml
    public void submitVysledok(View view) {
        zpracujVysledok();
    }

    //// Obsluha klavesnice
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

    // pocuvanie ake odosielacie tlacitko klavesnice je zmacknute, v tomto pripade go
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_GO) {
            zpracujVysledok();
            handled = true;
        }
        return handled;
    }

    // Play sound
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
        outState.putParcelable("priklad", priklad);
        outState.putInt("pokusov", pokusov);
        outState.putInt("spravne", spravne);
    }

    // Obnovujeme zapamatany stav
    //je zaujimave ze vysledokLocal ma hodnotu zachovanu bez toho ze by sme to riesili
    private void restoreMe(Bundle state){

        if  (state != null){

            priklad = state.getParcelable("priklad");
            pokusov = state.getInt("pokusov");
            spravne = state.getInt("spravne");
            // Nastavi text prikladu
            prikladText.setText(priklad.getPrikladString());
            textSpravneCounter.setText(spravne + "");
            textPokusovCounter.setText(pokusov + "");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kalkulacka, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Startovanie settings aktivity
            case R.id.settings:
                Intent intent;
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("preferencesType","kalkulacka");
                startActivity(intent);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.d("myTag", "This is onOptionsMenuClosed");
        getPriklad();
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
