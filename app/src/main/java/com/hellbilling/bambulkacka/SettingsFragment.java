package com.hellbilling.bambulkacka;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;
//import com.hellbilling.bambulkacka.ExampleSetting;

import java.util.HashMap;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceChangeListener {

    private static final String KEY_PREF_RESULT_START = "result_start";
    private static final String KEY_PREF_RESULT_STOP = "result_stop";
    private static final String KEY_PREF_NUMBER_START = "number_start";
    private static final String KEY_PREF_NUMBER_STOP = "number_stop";
    private static final String KEY_PREF_SIGN = "sign";
    private static final String KEY_PREF_EXTRA = "extra";
    private static final String KEY_PREF_USER_NAME = "user_name";
    private static final String KEY_PREF_REPEAT = "repeat";
    private static final String KEY_PREF_EXAMPLES_SETTING = "examples_setting_pref";

    private String preferencesType;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Find right preferences
        preferencesType = this.getArguments().getString("preferencesType");
        switch (preferencesType) {
            case "main":
                addPreferencesFromResource(R.xml.preferences_main);
                break;
            case "calculator":
                addPreferencesFromResource(R.xml.preferences_main);
                //addPreferencesFromResource(R.xml.preferences_calculator);
                break;
            default:
                addPreferencesFromResource(R.xml.preferences_main);
                break;
        }


        // Set examples_setting
        MultiSelectListPreference preferenceExamplesSetting = (MultiSelectListPreference) findPreference(KEY_PREF_EXAMPLES_SETTING);
        Context ctx = getActivity();
        HashMap ExamplesSettingData = ExampleSetting.getExamplesSettingAllKeyText(ctx);
        preferenceExamplesSetting.setEntries((CharSequence[]) ExamplesSettingData.get("long_name"));
        preferenceExamplesSetting.setEntryValues((CharSequence[]) ExamplesSettingData.get("_id"));

        // summary je pokec pod hlavnym textom polozky
        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference result_start = (EditTextPreference) findPreference(KEY_PREF_RESULT_START);
        result_start.setSummary(result_start.getText());

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference result_stop = (EditTextPreference) findPreference(KEY_PREF_RESULT_STOP);
        result_stop.setSummary(result_stop.getText());

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference number_start = (EditTextPreference) findPreference(KEY_PREF_NUMBER_START);
        number_start.setSummary(number_start.getText());

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference number_stop = (EditTextPreference) findPreference(KEY_PREF_NUMBER_STOP);
        number_stop.setSummary(number_stop.getText());

        // Register the ListPreference
        ListPreference preferenceExtra = (ListPreference) findPreference(KEY_PREF_EXTRA);
        preferenceExtra.setSummary(preferenceExtra.getEntry());
        preferenceExtra.setOnPreferenceChangeListener(this);


        if (preferencesType.equals("main")){
            // Nastavenie summary na aktualnu hodnotu
            EditTextPreference user_name = (EditTextPreference) findPreference(KEY_PREF_USER_NAME);
            user_name.setSummary(user_name.getText());

            // Nastavenie summary na aktualnu hodnotu
            EditTextPreference repeat = (EditTextPreference) findPreference(KEY_PREF_REPEAT);
            repeat.setSummary(repeat.getText());
        }
    }

    // Odtialto dole je tu na okamzitu zmenu v setting aktivite
    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (! numbersOK()){
            Toast.makeText(getActivity(), getString(R.string.wrong_input_numbers), Toast.LENGTH_SHORT).show();
        }
        // Change summary
        if (key.equals(KEY_PREF_RESULT_START)) {
            Preference result_start = findPreference(key);
            result_start.setSummary(sharedPreferences.getString(key, ""));
        }

        // Change summary
        if (key.equals(KEY_PREF_RESULT_STOP)) {
            Preference result_stop = findPreference(key);
            result_stop.setSummary(sharedPreferences.getString(key, ""));
        }

        // Change summary
        if (key.equals(KEY_PREF_NUMBER_START)) {
            Preference result_start = findPreference(key);
            result_start.setSummary(sharedPreferences.getString(key, ""));
        }

        // Change summary
        if (key.equals(KEY_PREF_NUMBER_STOP)) {
            Preference result_stop = findPreference(key);
            result_stop.setSummary(sharedPreferences.getString(key, ""));
        }

        if (preferencesType.equals("main")){
            // Change summary
            if (key.equals(KEY_PREF_USER_NAME)) {
                Preference username = findPreference(key);
                username.setSummary(sharedPreferences.getString(key, ""));
            }

            // Change summary
            if (key.equals(KEY_PREF_REPEAT)) {
                Preference repeat = findPreference(key);
                repeat.setSummary(sharedPreferences.getString(key, ""));
            }
        }
    }

    private boolean numbersOK(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int result_start = Integer.parseInt(sharedPref.getString("result_start", "0"));
        int result_stop = Integer.parseInt(sharedPref.getString("result_stop", "0"));
        int number_start = Integer.parseInt(sharedPref.getString("number_start", "0"));
        int number_stop = Integer.parseInt(sharedPref.getString("number_stop", "0"));
        if (result_start > result_stop){return false;}
        if (number_start > number_stop){return false;}
        return true;
    }

    // Toto obsluhuje vsetky ListPreference zo settingu
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ListPreference listPreference = (ListPreference) preference;

        int id = 0;
        for (int i = 0; i < listPreference.getEntryValues().length; i++) {
            if (listPreference.getEntryValues()[i].equals(newValue.toString())) {
                id = i;
                break;
            }
        }
        preference.setSummary(listPreference.getEntries()[id]);

        return true;
    }
}