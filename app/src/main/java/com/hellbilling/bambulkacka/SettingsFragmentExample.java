package com.hellbilling.bambulkacka;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;


public class SettingsFragmentExample extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceChangeListener {

    public static final String KEY_PREF_START = "start";
    public static final String KEY_PREF_STOP = "stop";
    public static final String KEY_PREF_SIGN = "sign";
    public static final String KEY_PREF_EXTRA = "extra";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_example);

        // EditTextPreferences: set current value of summary
        exampleEditTextPreferenceSetSummary(KEY_PREF_START);
        exampleEditTextPreferenceSetSummary(KEY_PREF_STOP);

        // ListPreferences: Register (onPreferenceChangeListener) and set current value of summary
        exampleRegisterListPreference(KEY_PREF_SIGN);
        exampleRegisterListPreference(KEY_PREF_EXTRA);

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
        if (key.equals(KEY_PREF_START)) {
            exampleEditTextPreferenceSetSummary(key, sharedPreferences.getString(key, ""));
        }
        else if (key.equals(KEY_PREF_STOP)) {
            exampleEditTextPreferenceSetSummary(key, sharedPreferences.getString(key, ""));
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

    // Change summary text of the preference
    private void exampleEditTextPreferenceSetSummary(String key, String value){
        EditTextPreference pref = (EditTextPreference) findPreference(key);
        pref.setSummary(value);
    }

    // Change summary text of the preference, add it's own text as default
    private void exampleEditTextPreferenceSetSummary(String key){
        EditTextPreference pref = (EditTextPreference) findPreference(key);
        exampleEditTextPreferenceSetSummary(key, pref.getText());
    }

    // Register List preference and change summary
    private void exampleRegisterListPreference(String key){
        ListPreference preferenceSign = (ListPreference) findPreference(key);
        preferenceSign.setSummary(preferenceSign.getEntry());
        preferenceSign.setOnPreferenceChangeListener(this);
    }

}