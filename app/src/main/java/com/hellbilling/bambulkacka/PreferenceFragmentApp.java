package com.hellbilling.bambulkacka;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;


public class PreferenceFragmentApp extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceChangeListener {

    public static final String KEY_PREF_USER_NAME = "user_name";
    public static final String KEY_PREF_REPEAT = "repeat";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_app);

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference user_name = (EditTextPreference) findPreference(KEY_PREF_USER_NAME);
        user_name.setSummary(user_name.getText());

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference repeat = (EditTextPreference) findPreference(KEY_PREF_REPEAT);
        repeat.setSummary(repeat.getText());
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