package com.hellbilling.bambulkacka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculator as a main part of the project
 */
public class Calculator extends ActionBarActivity implements EditText.OnEditorActionListener{

    /**
     * Database connectivity
     */
    private BambulkackaDB dbh;

    // Preferences
    /**
     * Range of generated numbers preference
     */
    private int prefResultStart, prefResultStop, prefNumberStart, prefNumberStop;
    /**
     * Sign preference, String of signs delimited by commas
     */
    private String prefSignsString;
    /**
     * Extra directive preference
     */
    private String prefExtra;
    // Other settings
    /**
     * User name preference
     */
    private String prefUserName;
    /**
     * Number of examples in one exercise preference
     */
    private int prefRepeat;
    /**
     * Play sound after result preference
     */
    private boolean prefSound;
    /**
     * Play sound after result preference
     */
    private boolean prefWaitForOkResult;/**!!!!!!!!!!!!!!!!!!
    /**
     * Examples setting ids preference
     */
    private Set<String> prefExamplesSetting;
    /**
     * Examples settins array to generate relevant examples
     */
    private List<ExampleSetting> examplesSettings = new ArrayList<>();

    // Widgets of activity
    /**
     * Text: Result of currentExample
     */
    private EditText wgResultLocal;
    /**
     * Text: Text of an currentExample withou result
     */
    private TextView wgExampleString;
    /**
     * Text: Error messages and comments for sent result
     */
    private TextView wgErrorString;
    /**
     * Text: Counter of all attempts
     */
    private TextView wgAttemptsCounterString;
    /**
     * Text: Counter of correct attempts
     */
    private TextView wgCorrectCounterString;

    /**
     * Current DB exercise _id
     */
    private long exerciseId;
    /**
     * Current currentExample
     */
    private Example currentExample;
    /**
     * Current DB currentExample _id
     */
    private long currentExampleId;
    /**
     * Examples from already done exercise
     */
    private ArrayList examplesIDs = new ArrayList();
    /**
     * Text: Counter of all attempts
     */
    private int attempts = 0;
    /**
     * Text: Counter of correct attempts
     */
    private int correct = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // Database connection
        Context ctx = getApplicationContext();
        dbh = new BambulkackaDB(ctx);

        // zistenie widgetov z obrazovky
        wgResultLocal =(EditText)findViewById(R.id.resultString);
        wgExampleString =(TextView)findViewById(R.id.exampleString);
        wgErrorString =(TextView)findViewById(R.id.errorString);
        wgAttemptsCounterString =(TextView)findViewById(R.id.attemptsCounterString);
        wgCorrectCounterString =(TextView)findViewById(R.id.correctCounterString);

        // tu sa nastavil listener onEditorAction na EditText, urcuje ako sa sprava tlacitko go
        wgResultLocal.setOnEditorActionListener(this);



        // Nacitaj settingy
        getSettings();

        // riesi zotavenie po zmene orientacie
        restoreMe(savedInstanceState);

        // Brand new exercise
        if (examplesIDs.size() == 0){
            getExamplesSettings();
            // Save new exercise
            exerciseId = dbh.insertExerciseStart(prefSignsString,Utils.getNow());

            // toto je velmi otazne
            if(currentExample == null) {
                getExample();
            }
        } // Exercise to repeat
        else {
            //Load all example_id from examples into examplesIDs
            getExample();
        }
    }

    private void getExample(){

        // Repeat examples from already saved exercise
        if (examplesIDs.size() == 0){
            // Chose exampleSetting for the example
            int exampleSettingUsed = 0;
            // Random
            if (examplesSettings.size() > 1) {
                exampleSettingUsed = Utils.generateRandomInt(0, examplesSettings.size() - 1);
            }

            //example = new Example(prefResultStart, prefResultStop, prefNumberStart, prefNumberStop, prefSignsString, prefExtra);
            ExampleSetting exampleSetting = examplesSettings.get(exampleSettingUsed);
            currentExample = new Example(exampleSetting);

            // Check example duplicity in exercise
            do {
                currentExample.getExample();
            } while (checkRepeat(exerciseId, currentExample));

            currentExampleId = dbh.insertExample(exerciseId, currentExample.getA(), currentExample.getB(), currentExample.getSignsString(), currentExample.getResult());
            // Set up text of the example
            wgExampleString.setText(currentExample.getExampleString());
        }
        // Repeat examples from already saved exercise
        else {
        }
    }

    /**
     * Check input value of result and evaluate it
     */
    private void eveluateExample() {

        String resultLocalStr = wgResultLocal.getText().toString();
        int vysledokLocalInt;

        wgErrorString.setTextColor(getResources().getColor(R.color.default_color));
        wgErrorString.setText("");

        // vysledok je prazdny
        if (resultLocalStr.length() == 0){
            //vysledokLocalInt = 0;
            wgErrorString.setText(prefUserName + " , " + getString(R.string.empty_result) + ".");
            return;
        }

        // vysledok nie je cislo
        if ( ! resultLocalStr.matches("\\d+")) {
            //vysledokLocalInt = 0;
            wgErrorString.setText(prefUserName + ", " + getString(R.string.natural_numbers) + "!");
            wgResultLocal.setText("");
            return;
        }
        else {
            try {
                vysledokLocalInt = Integer.parseInt(resultLocalStr);
            } catch(NumberFormatException nfe) {
                wgErrorString.setText(getString(R.string.num_limit) + ": \n" + nfe);
                wgResultLocal.setText("");
                return;
            }
        }

        // Vysledok dobre
        if (vysledokLocalInt == currentExample.getResult()){
            Log.d("++", "vysledok je dobre");
            // prehrajeme ok zvuk
            if (prefSound){playSound("ok");}

            wgCorrectCounterString.setText(++correct + "");
            wgAttemptsCounterString.setText(++attempts + "");
            wgErrorString.setTextColor(getResources().getColor(R.color.spravne_color));
            wgErrorString.setText(prefUserName + ", " + getString(R.string.genius) + ", " + currentExample.getExampleStringFull() + ", ides dalej.");

            // Save attempt
            dbh.insertAttempt(currentExampleId, vysledokLocalInt, Utils.getNow(), 1);

            // ak je dokoncene tak otvori resume aktivitu
            if (correct == prefRepeat){
                Intent intent;
                intent = new Intent(this, ResumeActivity.class);
                intent.putExtra("correct", correct);
                intent.putExtra("attempts", attempts);

                // Save exercise end
                long id = dbh.updateExerciseEnd(exerciseId,Utils.getNow());

                startActivity(intent);
            } // inak da novy currentExample
            else {
                getExample();
            }
        }
        // Vysledok zle
        else {
            Log.d("--", "vysledok " + vysledokLocalInt + " je zle");
            // Prehrajeme NOK zvuk
            if (prefSound){playSound("nok");}
            wgAttemptsCounterString.setText(++attempts + "");
            //wgErrorString.setTextColor(getResources().getIdentifier("nespravne_color", "color", getPackageName()));
            wgErrorString.setTextColor(getResources().getColor(R.color.nespravne_color));
            wgErrorString.setText(prefUserName + ", " + prefUserName + ". " + getString(R.string.compl_wrong) + "!");

            // Save attempt
            dbh.insertAttempt(currentExampleId, vysledokLocalInt, Utils.getNow(), 0);
        }

        // Vynuluj editText
        wgResultLocal.setText("");

    }


    /**
     * Check if this currentExample has been already generated for this exercise
     * we don't want prefRepeat the same currentExample
     */
    private boolean checkRepeat(long exercise_id, Example example){

        Context ctx = this;
        BambulkackaDB dbh = new BambulkackaDB(ctx);
        Cursor examplesCursor = dbh.getResultExamples(String.valueOf(exercise_id));

        // Current currentExample
        int priklad_a = example.getA();
        int priklad_b = example.getB();
        // if Cursor is contains results
        if (examplesCursor != null) {
            // move cursor to first row
            if (examplesCursor.moveToFirst()) {
                do {
                    // Database currentExample
                    String a = examplesCursor.getString(examplesCursor.getColumnIndex("a"));
                    String b = examplesCursor.getString(examplesCursor.getColumnIndex("b"));
                    if (Integer.parseInt(a) == priklad_a && Integer.parseInt(b) == priklad_b){
                        return true;
                    }
                } while (examplesCursor.moveToNext());
            }
        }
        dbh.close();
        return false;
    }

    /**
     * Load preferences
     */
    private void getSettings(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        prefResultStart = Integer.parseInt(sharedPref.getString("result_start", getString(R.string.result_start)));
        prefResultStop = Integer.parseInt(sharedPref.getString("result_stop", getString(R.string.result_stop)));
        prefNumberStart = Integer.parseInt(sharedPref.getString("number_start", getString(R.string.number_start)));
        prefNumberStop = Integer.parseInt(sharedPref.getString("number_stop", getString(R.string.number_stop)));
        prefRepeat = Integer.parseInt(sharedPref.getString("repeat", getString(R.string.repeat)));

        //prefSignsString = sharedPref.getString("sign", "all");
        Set<String> signs = sharedPref.getStringSet("signs_pref",new HashSet<String>());
        String[] signsArray = signs.toArray(new String[signs.size()]);
        prefSignsString = TextUtils.join(",", signsArray);

        prefExtra = sharedPref.getString("extra", getString(R.string.extra));
        prefUserName = sharedPref.getString("user_name", getString(R.string.user_name));
        prefSound = sharedPref.getBoolean("sound", Boolean.parseBoolean(getString(R.string.sound)));
        prefWaitForOkResult = sharedPref.getBoolean("wait_for_ok_result", Boolean.parseBoolean(getString(R.string.sound)));
        Toast.makeText(this, "prefWaitForOkResult : " + prefWaitForOkResult, Toast.LENGTH_LONG).show();
        prefExamplesSetting = sharedPref.getStringSet("examples_setting_pref",new HashSet<String>());

    }

    /**
     * Load examplesSettings ArrayList by ExampleSetting objects
     */
    private void getExamplesSettings(){
        // LOAD DATA FROM prefExamplesSetting
        if(prefExamplesSetting.size()>0){
            List<String> ids = new ArrayList<>();
            for (String subset : prefExamplesSetting) {
                ids.add(subset);
            }
            Context ctx = getApplicationContext();
            BambulkackaDB dbh = new BambulkackaDB(ctx);
            Cursor cursor = dbh.getExamplesSettingAll(ids);

            if (cursor.moveToFirst()) {
                do {
                    //Load wanted ExampleSetting instances
                    int result_start = cursor.getInt(cursor.getColumnIndex(BambulkackaContract.TbExampleSettings.COLUMN_NAME_RESULT_START));
                    int result_stop = cursor.getInt(cursor.getColumnIndex(BambulkackaContract.TbExampleSettings.COLUMN_NAME_RESULT_STOP));
                    int number_start = cursor.getInt(cursor.getColumnIndex(BambulkackaContract.TbExampleSettings.COLUMN_NAME_NUMBER_START));
                    int number_stop = cursor.getInt(cursor.getColumnIndex(BambulkackaContract.TbExampleSettings.COLUMN_NAME_NUMBER_STOP));
                    String signs = cursor.getString(cursor.getColumnIndex(BambulkackaContract.TbExampleSettings.COLUMN_NAME_SIGNS_STRING));
                    String extra = cursor.getString(cursor.getColumnIndex(BambulkackaContract.TbExampleSettings.COLUMN_NAME_EXTRA));
                    ExampleSetting myExampleSetting = new ExampleSetting(result_start, result_stop, number_start, number_stop, signs,extra);
                    examplesSettings.add(myExampleSetting);

                } while (cursor.moveToNext());
            }
            cursor.close();
            dbh.close();
        }
        // prefExamplesSetting is empty, build custom setting
        else {
            ExampleSetting myExampleSetting = new ExampleSetting(prefResultStart, prefResultStop, prefNumberStart, prefNumberStop, prefSignsString, prefExtra);
            examplesSettings.add(myExampleSetting);
        }
    }
    /**
     * Submit button handling
     * <p>
     * This method is assigned in activity_calculator.xml
     * @param view view no pokec
     */
    public void submitResult(View view) {
        eveluateExample();
    }

    /**
     * Keyboard handling
     */
    // Toto moze byt divne, ako vieme ku ktoremu editTextu to otvara tu klavesnicu?
    void showSoftKeyboard() {
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

    /**
     * Play prefSound
     * @param filename source of prefSound
     */
    private void playSound (String filename) {
        // Find file as resource
        int res = getResources().getIdentifier(filename, "raw", getPackageName());
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), res);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
    }

    private void goHome(){
        // Save exercise end
        dbh.updateExerciseEnd(exerciseId,Utils.getNow());
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Obnovujeme zapamatany stav
    //je zaujimave ze wgResultLocal ma hodnotu zachovanu bez toho ze by sme to riesili
    private void restoreMe(Bundle state){

        if  (state != null){

            currentExample = state.getParcelable("currentExample");
            attempts = state.getInt("attempts");
            correct = state.getInt("correct");
            // Nastavi text prikladu
            wgExampleString.setText(currentExample.getExampleString());
            wgCorrectCounterString.setText(correct + "");
            wgAttemptsCounterString.setText(attempts + "");
        }

    }


    /**
     * Listener for submit button, action GO
     * @param view view bez pokecu
     * @param actionId GO,or something else?
     * @param event ???
     * @return true if action is GO
     */
    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_GO) {
            eveluateExample();
            handled = true;
        }
        return handled;
    }

    //// Obsluha zapamatania
    // ukladame premenne
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("currentExample", currentExample);
        outState.putInt("attempts", attempts);
        outState.putInt("correct", correct);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Startovanie settings aktivity
            case R.id.settings:
                Intent intent;
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("preferencesType","calculator");
                startActivity(intent);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.d("myTag", "This is onOptionsMenuClosed");
        getExample();
    }

    @Override
    public void onBackPressed(){
        goHome();
    }
}
