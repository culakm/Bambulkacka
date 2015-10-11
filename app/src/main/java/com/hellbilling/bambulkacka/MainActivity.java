package com.hellbilling.bambulkacka;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends ActionBarActivity {



    /*


    dorobit ovladanie klavesnice


     */



    // default hodnota pre range
    String radioStatus = "do100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonSendsMessage(View view) {

        //RadioGroup wRadioGroup=(RadioGroup)findViewById(R.id.moj_radio_group_id);

        Intent intent;
        intent = new Intent(this, kalkulacka.class);
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
