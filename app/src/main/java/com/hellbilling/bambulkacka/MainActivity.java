package com.hellbilling.bambulkacka;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    /*TODO

    skontrolovat player, v settings povolit a zastavit zvuky, nastavit hlasitost?

    ako updatovat settings hodnoty ak ich nastavujem v kalkulacka.java?

    vyskusat uplne prvotne nastavenie pre znamienko a extra !!

    zmena settingsov aj z kalkulacky, len prikladovych, nie user_name

    nejaku konstantu pre default value start, stop. znamienko, extra

    custom back v action bare

    zkontrolovat spravanie sa MediaPlayeru podla vypisov na konzole
        E/MediaPlayer-JNI﹕ QCMediaPlayer mediaplayer NOT present
        E/MediaPlayer﹕ Should have subtitle controller already set
        W/MediaPlayer-JNI﹕ MediaPlayer finalized without being released

    prirobit pocitadlo prikladov (uchovavat objeky prikladov?)

    po splneni urciteho poctu prikladov spustit youtube

    pouzit custom preference, posuvace, hodiny, kalendar,,,,,,
    http://developer.android.com/guide/topics/ui/settings.html#Custom
    http://stackoverflow.com/questions/16108609/android-creating-custom-preference

    pozret algoritmus prikladov cez 10

    co je to context??
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nastavi defaultne hodnoty preference
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    public void buttonSendsMessage(View view) {
        Intent intent;
        intent = new Intent(this, Kalkulacka.class);
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

        switch (item.getItemId()) {
            case R.id.add:
                return(true);
            // Startovanie settings aktivity
            case R.id.settings:
                Intent intent;
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
    }
}
