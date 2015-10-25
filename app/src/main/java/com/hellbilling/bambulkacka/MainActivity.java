package com.hellbilling.bambulkacka;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import java.lang.reflect.Method;

public class MainActivity extends ActionBarActivity {



    /*

    urobit  menu kde bude jedno settings


    v settings nastavit parametre po jednom
                nastavit vsetky parametre v jednom okne
        - nastavovat typ prikladov
        - nastavit defaultny typ prikladov
        - nastavovat oslovenie

    custom back v action bare

    dorobit priklady cez 10


    prirobit pocitadlo prikladov (uchovavat objeky prikladov?)

    po splneni urciteho poctu prikladov spustit youtube




    co je to context??
     */

    // default hodnota pre range
    String radioStatus = "do100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonSendsMessage(View view) {

        Intent intent;
        intent = new Intent(this, Kalkulacka.class);
        intent.putExtra("calkStatus",radioStatus);
        startActivity(intent);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                if (checked)
                    radioStatus = "do20";
                break;
            case R.id.radioButton2:
                if (checked)
                    radioStatus = "do100";
                break;
            case R.id.radioButton3:
                if (checked)
                    radioStatus = "nad100";
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Toto cele je LEN na zobrazenie ikony v menu!
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e("sdafd", "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.add:

                return(true);

            case R.id.settings:
                /*Intent intent;
                intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                */
                return(true);
        }

        return(super.onOptionsItemSelected(item));
        /*
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        */
    }
}
