package com.hellbilling.bambulkacka;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

// Since the version 22.1.0, the class ActionBarActivity is deprecated. You should use AppCompatActivity
// http://stackoverflow.com/questions/29890530/actionbaractivity-is-deprecated-android-studio
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nastavi defaultne hodnoty preference
        PreferenceManager.setDefaultValues(this, R.xml.preferences_main, false);
    }

    public void buttonSendsMessage(View view) {
        Intent intent;
        intent = new Intent(this, Calculator.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*/ Toto cele je LEN na zobrazenie ikony v menu!
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
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.results:
                intent = new Intent(this, ResultsActivity.class);
                startActivity(intent);
                return(true);
            // Startovanie settings aktivity
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("preferencesType","main");
                startActivity(intent);
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
    }
}
