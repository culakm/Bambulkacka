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

    /*TODO

    dorobit zobrazovanie ok.nok v resultsoch
    https://www.zdrojak.cz/clanky/vyvijime-pro-android-fragmenty-a-sqlite-databaze/
    pozret ako je urobeny kontrakt http://stackoverflow.com/questions/17451931/how-to-use-a-contract-class-in-android,
    zistit ako inteligentne nacitavat zoznamy
    dodat databazu pre uchovavanie vysledkov
    ://www.zdrojak.cz/clanky/vyvijime-pro-android-fragmenty-a-sqlite-databaze/
    http://developer.android.com/training/basics/data-storage/databases.html
    zobrazovat vyhodnotenie asi z menu a z resume activity


    v kalkulacka.java skusit vyriesit onOptionsMenuClosed aby to podla nastaveni cisel a znamienka a ostatnych veci zmenilo priklad


    skontrolovat player, v settings povolit a zastavit zvuky, nastavit hlasitost?

    ako updatovat settings hodnoty ak ich nastavujem v kalkulacka.java?

    vyskusat uplne prvotne nastavenie pre znamienko a extra !!

    zmena settingsov aj z kalkulacky, len prikladovych, nie user_name

    nejaku konstantu pre default value start, stop. znamienko, extra
    inteligentne obhospodarovat konstanty

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


    co je to context??
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nastavi defaultne hodnoty preference
        PreferenceManager.setDefaultValues(this, R.xml.preferences_main, false);
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
