package com.hellbilling.bambulkacka;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;


public class PreferenceFragmentExample extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceChangeListener {

    public static final String KEY_PREF_START = "start";
    public static final String KEY_PREF_STOP = "stop";
    public static final String KEY_PREF_SIGN = "sign";
    public static final String KEY_PREF_EXTRA = "extra";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_example);

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference start = (EditTextPreference) findPreference(KEY_PREF_START);
        start.setSummary(start.getText());

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference stop = (EditTextPreference) findPreference(KEY_PREF_STOP);
        stop.setSummary(stop.getText());

        // Register the ListPreference
        ListPreference preferenceZnamienko = (ListPreference) findPreference(KEY_PREF_SIGN);
        preferenceZnamienko.setSummary(preferenceZnamienko.getEntry());
        preferenceZnamienko.setOnPreferenceChangeListener(this);

        // Register the ListPreference
        ListPreference preferenceExtra = (ListPreference) findPreference(KEY_PREF_EXTRA);
        preferenceExtra.setSummary(preferenceExtra.getEntry());
        preferenceExtra.setOnPreferenceChangeListener(this);

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
            Preference username = findPreference(key);
            username.setSummary(sharedPreferences.getString(key, ""));
        }

        // Change summary
        if (key.equals(KEY_PREF_STOP)) {
            Preference username = findPreference(key);
            username.setSummary(sharedPreferences.getString(key, ""));
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