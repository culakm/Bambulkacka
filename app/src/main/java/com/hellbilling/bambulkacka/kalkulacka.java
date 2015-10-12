package com.hellbilling.bambulkacka;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//public class kalkulacka extends ActionBarActivity {
public class kalkulacka extends ActionBarActivity implements EditText.OnEditorActionListener{

    // typ prikladu
    String calkStatus;
    EditText vysledokLocal;
    TextView prikladText;
    TextView errorText;

    // rozsah generovanych cisel
    int start = 0,stop;
    // aktualny priklad
    Priklad priklad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulacka);

        // nacitanie z parametrov
        calkStatus = getIntent().getExtras().getString("calkStatus");

        // zistenie widgetov z obrazovky
        vysledokLocal =(EditText)findViewById(R.id.vysledok_text);
        prikladText =(TextView)findViewById(R.id.prikladText);
        errorText =(TextView)findViewById(R.id.errorText);

        // tu sa nastavil listener onEditorAction na EditText
        vysledokLocal.setOnEditorActionListener(this);

        // nastavime stop rozsahu generovanych cisel
        setStartStop();
        // nacitame aktualny priklad
        getPriklad();
    }

    // pocuvanie ake odosielacie tlacitko klavesnice je zmacknute, v tomto pripade go
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        Toast.makeText(getApplicationContext(), " klikol som go", Toast.LENGTH_SHORT).show();
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
            errorText.setText("Baruska, ty si genius, " + priklad.getCelyPrikladString() + ", ides dalej.");
            getPriklad();
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok je zle");
            // Prehrajeme NOK zvuk
            playSound("nok");
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
            case "nad100":  stop = 1000;
                break;
            default: stop = 100;
                break;
        }

    }

    // prehrajeme zvuk
    private void playSound (String filename) {
        // Find file as resource
        int res = getResources().getIdentifier(filename, "raw", getPackageName());
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), res);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
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
