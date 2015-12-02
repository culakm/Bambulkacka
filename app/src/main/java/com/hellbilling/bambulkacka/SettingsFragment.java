package com.hellbilling.bambulkacka;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceChangeListener {

    public static final String KEY_PREF_RESULT_START = "result_start";
    public static final String KEY_PREF_RESULT_STOP = "result_stop";
    public static final String KEY_PREF_NUMBER_START = "number_start";
    public static final String KEY_PREF_NUMBER_STOP = "number_stop";
    public static final String KEY_PREF_SIGN = "sign";
    public static final String KEY_PREF_EXTRA = "extra";
    public static final String KEY_PREF_USER_NAME = "user_name";
    public static final String KEY_PREF_REPEAT = "repeat";

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
            case "kalkulacka":
                addPreferencesFromResource(R.xml.preferences_calculator);
                break;
            default:
                addPreferencesFromResource(R.xml.preferences_main);
                break;
        }

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
        ListPreference preferenceSign = (ListPreference) findPreference(KEY_PREF_SIGN);
        preferenceSign.setSummary(preferenceSign.getEntry());
        preferenceSign.setOnPreferenceChangeListener(this);

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