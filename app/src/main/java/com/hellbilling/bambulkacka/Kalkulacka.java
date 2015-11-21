package com.hellbilling.bambulkacka;

import android.content.Intent;
import android.content.SharedPreferences;
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

    // Nastavenia pre priklady
    // rozsah generovanych cisel
    private int prikladStart,prikladStop;
    // Znamienko
    private String prikladZnamienko;
    // Extra
    private String prikladExtra;
    // Ine nastavenie
    private String userName;
    // pocet prikladov
    private int repeat;

    // Pristup k settingu
    SharedPreferences sharedPref;

    // Widgety aktivity
    // Tu zadavame vysledok
    EditText vysledokLocal;
    // text zadania prikladu
    TextView prikladText;
    // Chybove hlasenia, a komentare k odoslanemu vysledku
    TextView errorText;
    // Zobrazovanie poctu vsetkych pokusov
    TextView textPokusovCounter;
    // Zobrazovanie poctu spravnych pokusov
    TextView textSpravneCounter;

    // aktualny priklad
    Priklad priklad;

    // Pocitadla
    int pokusov = 0;
    int spravne = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulacka);

        // zistenie widgetov z obrazovky
        vysledokLocal =(EditText)findViewById(R.id.vysledok_text);
        prikladText =(TextView)findViewById(R.id.prikladText);
        errorText =(TextView)findViewById(R.id.errorText);
        textPokusovCounter =(TextView)findViewById(R.id.textPokusovCounter);
        textSpravneCounter =(TextView)findViewById(R.id.textSpravneCounter);

        // tu sa nastavil listener onEditorAction na EditText, urcuje ako sa sprava tlacitko go
        vysledokLocal.setOnEditorActionListener(this);

        // Nacitaj settingy
        //getSettings();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        // Pocet opakovani
        repeat = Integer.parseInt(sharedPref.getString("repeat", "10"));

        // riesi zotavenie po zmene orientacie
        restoreMe(savedInstanceState);

        // nacitame aktualny priklad ak este neexistuje, inak sa taha z restoreMe
        if(priklad==null) {
            getPriklad();
        }
        // otvorim klavesnicu
        showSoftKeyboard();
    }

    // Nacita priklad
    private void getPriklad() {

        prikladStart = Integer.parseInt(sharedPref.getString("start", "0"));
        prikladStop = Integer.parseInt(sharedPref.getString("stop", "100"));
        prikladZnamienko = sharedPref.getString("znamienko", "+/-");
        prikladExtra = sharedPref.getString("extra", "nic");
        userName = sharedPref.getString("user_name", "Detisko");

        priklad = new Priklad(prikladStart,prikladStop,prikladZnamienko,prikladExtra);
        priklad.getCisla();
        // Nastavi text prikladu
        prikladText.setText(priklad.getPrikladString());
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
            errorText.setText(userName + " , nic nie je vyplnene, daj este raz.");
            return;
        }

        // vysledok nie je cislo
        if ( ! vysledokLocalStr.matches("\\d+")) {
            //vysledokLocalInt = 0;
            errorText.setText(userName + ", musis zadat cele cislo, davaj!");
            vysledokLocal.setText("");
            return;
        }
        else {
            try {
                vysledokLocalInt = Integer.parseInt(vysledokLocalStr);
            } catch(NumberFormatException nfe) {
                errorText.setText("Vysledok musi byt mensi ako 2147483647 alebo je tu ina chyba: \n" + nfe);
                vysledokLocal.setText("");
                return;
            }
        }

        // Vysledok dobre
        if (vysledokLocalInt == priklad.getVysledok()){
            Log.d("++", "vysledok je dobre");
            // prehrajeme ok zvuk
            if (sharedPref.getBoolean("sound", true)){playSound("ok");}

            textSpravneCounter.setText(++spravne + "");
            textPokusovCounter.setText(++pokusov + "");
            errorText.setTextColor(getResources().getColor(R.color.spravne_color));
            errorText.setText(userName + ", ty si genius, " + priklad.getCelyPrikladString() + ", ides dalej.");

            // ak je dokoncene tak otvori resume aktivitu
            if (spravne == repeat){
                Intent intent;
                intent = new Intent(this, ResumeActivity.class);
                intent.putExtra("spravne",spravne);
                intent.putExtra("pokusov",pokusov);

                startActivity(intent);
            } // inak da novy priklad
            else {
                getPriklad();
            }
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok je zle");
            // Prehrajeme NOK zvuk
            if (sharedPref.getBoolean("sound", true)){playSound("nok");}
            textPokusovCounter.setText(++pokusov + "");
            //errorText.setTextColor(getResources().getIdentifier("nespravne_color", "color", getPackageName()));
            errorText.setTextColor(getResources().getColor(R.color.nespravne_color));
            errorText.setText(userName + ", " + userName + ". Cele zle, musis si to zopakovat este raz!");
        }


        // Vynuluj editText
        vysledokLocal.setText("");

    }

    // Natiahne settings, nikdy nepouzite, settingy sa natahuju v getPriklad
    private void getSettings(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        prikladStart = Integer.parseInt(sharedPref.getString("start", "0"));
        prikladStop = Integer.parseInt(sharedPref.getString("stop", "100"));
        prikladZnamienko = sharedPref.getString("znamienko", "+/-");
        prikladExtra = sharedPref.getString("extra", "nic");
        userName = sharedPref.getString("user_name", "Detisko");
        //Toast.makeText(getApplicationContext(), "start: " + prikladStart + ", stop: " + prikladStop + ", znamienko: " + prikladZnamienko + ", extra: " + prikladExtra + ", username: " + userName, Toast.LENGTH_SHORT).show();
        //Log.d( "settings: " , "start: " + prikladStart + ", stop: " + prikladStop + ", znamienko: " + prikladZnamienko + ", extra: " + prikladExtra + ", username: " + userName);
    }

    //// Obsluha odosielacieho tlacitka, toto je mu priradene v activity_kalkulacka.xml
    public void submitVysledok(View view) {
        zpracujVysledok();
    }

    //// Obsluha klavesnice
    // Toto moze byt divne, ako vieme ku ktoremu editTextu to otvara tu klavesnicu?
    public void showSoftKeyboard() {
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

    //// Prehrajeme zvuk
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
            Log.d("--", "pokusov:"+priklad.getPrikladString());
            // Nastavi text prikladu
            prikladText.setText(priklad.getPrikladString());
            textSpravneCounter.setText(spravne + "");
            textPokusovCounter.setText(pokusov + "");
        }

    }

    // Nevyuzivane
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
                intent = new Intent(this, PreferencesActivity.class);
                intent.putExtra("preferencesType","kalkulacka");
                startActivity(intent);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }


}
