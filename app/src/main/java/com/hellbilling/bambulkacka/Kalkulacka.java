package com.hellbilling.bambulkacka;

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
import android.widget.Toast;

public class Kalkulacka extends ActionBarActivity implements EditText.OnEditorActionListener{

    // Typ prikladu
    String calkStatus;
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

    // rozsah generovanych cisel
    int start = 0,stop;
    // aktualny priklad
    Priklad priklad;

    // Pocitadla
    int pokusov = 0;
    int spravne = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulacka);

        // Nacitanie parametrov z aktivity odkial tato aktivita bola volana
        calkStatus = getIntent().getExtras().getString("calkStatus");

        // zistenie widgetov z obrazovky
        vysledokLocal =(EditText)findViewById(R.id.vysledok_text);
        prikladText =(TextView)findViewById(R.id.prikladText);
        errorText =(TextView)findViewById(R.id.errorText);
        textPokusovCounter =(TextView)findViewById(R.id.textPokusovCounter);
        textSpravneCounter =(TextView)findViewById(R.id.textSpravneCounter);

        // tu sa nastavil listener onEditorAction na EditText
        vysledokLocal.setOnEditorActionListener(this);

        // Nacitaj settingy
        getSettings();

        // riesi zotavenie po zmene orientacie
        restoreMe(savedInstanceState);

        // nastavime stop rozsahu generovanych cisel
        setStartStop();

        // nacitame aktualny priklad ak este neexistuje, inak sa taha z restoreMe
        if(priklad==null) {
            getPriklad();
        }
        // otvorim klavesnicu
        showSoftKeyboard();
    }


    private void getSettings(){
        //////////////
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean syncConnPref1 = sharedPref.getBoolean("pref_sync1", true);
        String syncConnPref3 = sharedPref.getString("pref_sync3", "");
        Toast.makeText(getApplicationContext(), "String  pref_sync1 zo settingsov: " + syncConnPref1.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "String  pref_sync3 zo settingsov: " + syncConnPref3, Toast.LENGTH_SHORT).show();
        //////////
    }

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

    //  handlovanie odosielacieho tlacitka
    public void submitVysledok(View view) {
        zpracujVysledok();
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
            errorText.setText("Bambulina, zadala si prazdny vysledok, daj este raz.");
            return;
        }

        // vysledok nie je cislo
        if ( ! vysledokLocalStr.matches("\\d+")) {
            //vysledokLocalInt = 0;
            errorText.setText("Prskon maly, musis zadat cele cislo, davaj!");
            vysledokLocal.setText("");
            return;
        }
        else {
            vysledokLocalInt = Integer.parseInt(vysledokLocalStr);
        }

        // Vysledok dobre
        if (vysledokLocalInt == priklad.getVysledok()){
            Log.d("++", "vysledok je dobre");
            // prehrajeme ok zvuk
            playSound("ok");

            textSpravneCounter.setText(++spravne + "");
            textPokusovCounter.setText(++pokusov + "");
            errorText.setTextColor(getResources().getColor(R.color.spravne_color));
            errorText.setText("Baruska, ty si genius, " + priklad.getCelyPrikladString() + ", ides dalej.");
            getPriklad();
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok je zle");
            // Prehrajeme NOK zvuk
            playSound("nok");
            textPokusovCounter.setText(++pokusov + "");
            //errorText.setTextColor(getResources().getIdentifier("nespravne_color", "color", getPackageName()));
            errorText.setTextColor(getResources().getColor(R.color.nespravne_color));
            errorText.setText("Cele zle, este raz!");
        }

        // Vynuluj editText
        vysledokLocal.setText("");

    }

    // Nacita priklad
    private void getPriklad() {
        priklad = new Priklad(start,stop);
        priklad.getCisla();
        // Nastavi text prikladu
        prikladText.setText(priklad.getPrikladString());
    }

    // Nastavi stop pre priklad (start je default 0)
    private void setStartStop() {
        switch (calkStatus) {
            case "do20":  stop = 20;
                break;
            case "do100":  stop = 100;
                break;
            case "do100cez10":  stop = 100;
                break;
            case "nad100":  stop = 1000;
                break;
            default: stop = 100;
                break;
        }

    }

    private void updateVysledky (){

    }

    // prehrajeme zvuk
    private void playSound (String filename) {
        // Find file as resource
        int res = getResources().getIdentifier(filename, "raw", getPackageName());
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), res);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
    }


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kalkulacka, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
