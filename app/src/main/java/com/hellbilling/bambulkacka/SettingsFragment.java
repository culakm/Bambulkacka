package com.hellbilling.bambulkacka;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {

/*
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
*/



        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Register the ListPreference
        //ListPreference preferenceLanguage = (ListPreference) findPreference("pref.language");
        //preferenceLanguage.setSummary(preferenceLanguage.getEntry());
        //preferenceLanguage.setOnPreferenceChangeListener(this);

        // Nastavenie summary na aktualnu hodnotu
        EditTextPreference user_name = (EditTextPreference) findPreference("user_name");
        user_name.setSummary(user_name.getText());
    }
}