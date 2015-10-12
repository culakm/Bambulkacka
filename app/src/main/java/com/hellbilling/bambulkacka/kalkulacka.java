package com.hellbilling.bambulkacka;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class kalkulacka extends ActionBarActivity {

    String calkStatus;
    TextView prikladText;
    TextView errorText;

    int start = 0,stop;
    Priklad priklad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulacka);

        calkStatus = getIntent().getExtras().getString("calkStatus");
        //Toast.makeText(getApplicationContext(), " PAKO calkStatus : " + calkStatus, Toast.LENGTH_SHORT).show();
        prikladText =(TextView)findViewById(R.id.prikladText);
        errorText =(TextView)findViewById(R.id.errorText);

        setStartStop();
        getPriklad();

    }

    public void submitVysledok(View view) throws InterruptedException {

        EditText vysledokLocal =(EditText)findViewById(R.id.vysledok_text);
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
            playSound("ok");
            errorText.setText("Baruska, ty si genius, " + priklad.getCelyPrikladString() + ", ides dalej.");
            getPriklad();
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok je zle");
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
