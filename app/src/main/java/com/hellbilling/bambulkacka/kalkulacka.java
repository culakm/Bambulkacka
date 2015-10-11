package com.hellbilling.bambulkacka;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import static java.lang.Thread.sleep;


public class kalkulacka extends ActionBarActivity {


    String calkStatus;
    int start = 0,stop;
    Priklad priklad;
    //int a,b, prikladVysledok;
    String znamienko;

    TextView prikladText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulacka);

        calkStatus = getIntent().getExtras().getString("calkStatus");
        prikladText =(TextView)findViewById(R.id.prikladText);

        setStartStop();
        getPriklad();
    }




    private void getPriklad() {
        priklad = new Priklad(start,stop);
        priklad.getCisla();

        prikladText.setText(priklad.getA() + priklad.getZnamienko() + priklad.getB() + " = " + priklad.getVysledok());
    }

    public void submitVysledok(View view) throws InterruptedException {

        EditText vysledokLocal =(EditText)findViewById(R.id.vysledok_text);
        String vysledokLocalStr = vysledokLocal.getText().toString();
        int vysledokLocalInt;

        // vysledok je nenulovy
        if (vysledokLocalStr.length() == 0){
            vysledokLocalInt = 0;
        }

        // vysledok je cislo
        if ( ! vysledokLocalStr.matches("\\d+")) {
            vysledokLocalInt = 0;
        }
        else {
            vysledokLocalInt = Integer.parseInt(vysledokLocalStr);
        }

        // Vysledok dobre
        if (vysledokLocalInt == priklad.getVysledok()){
            Log.d("--", "vysledok je dobre");
            prikladText.setText("Dobre " + vysledokLocalStr);
            getPriklad();
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok je zle");
            prikladText.setText("Zle, skus dalsi priklad");
        }

        vysledokLocal.setText("");

    }

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
