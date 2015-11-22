package com.hellbilling.bambulkacka;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    /*

http://developer.android.com/guide/topics/ui/menus.html

    ###FUNKCNOST
    niekedy nefunguje preberanie spravneho znamienka !!!
    okrem start a stop vysledku zadavat aj start a stop cislam !!!!
    nejaku konstantu pre default value start, stop. znamienko, extra
    prirobit pocitadlo prikladov (uchovavat objeky prikladov?)
    po splneni urciteho poctu prikladov spustit youtube

    ###MENU-ACTION BAR
    action bar pre option menu, jednotlive hlavicky settings, pokec, back
http://www.101apps.co.za/index.php/articles/preference-settings.html


    ### SETTINGS
    ako mat uz otvoreny headerFragment v settingsoch?
    back v jednotlivych fragmentoch
    preco nejdu ikony v settingsoch? preferences header android:icon="@drawable/cw"
    ako updatovat settings hodnoty ak ich nastavujem v kalkulacka.java? neviem co som tym chcel povedat
    pouzit custom preference, posuvace, hodiny, kalendar,,,,,,
    http://developer.android.com/guide/topics/ui/settings.html#Custom
    http://stackoverflow.com/questions/16108609/android-creating-custom-preference
    ### ZVUK
    zkontrolovat spravanie sa MediaPlayeru podla vypisov na konzole
        E/MediaPlayer-JNI﹕ QCMediaPlayer mediaplayer NOT present
        E/MediaPlayer﹕ Should have subtitle controller already set
        W/MediaPlayer-JNI﹕ MediaPlayer finalized without being released

    alebo na prskonovom
    11-22 08:18:36.966    7369-7369/com.hellbilling.bambulkacka I/MediaPlayer﹕ Don't send intent. msg.arg1 = 0, msg.arg2 = 0
11-22 08:18:36.966    7369-7369/com.hellbilling.bambulkacka E/MediaPlayer﹕ Should have subtitle controller already set
    skontrolovat player, v settings povolit a zastavit zvuky, nastavit hlasitost RingtonePreference?

    ### ODOVZDAVANIE
    vyskusat uplne prvotne nastavenie pre znamienko a extra !!

    ### OSTATNE
    definovanie konstant??? co to je a ako sa to pouziva?
    if (Constants.SETTING_UPDATE.equals(settings))
        http://gmariotti.blogspot.sk/2013/01/preferenceactivity-preferencefragment_28.html

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
/*
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
*/

    // Just start settings activity
    public void startSettings(MenuItem item){
        Intent intent;
        intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("preferencesType","main");
        startActivity(intent);
    }
}
