package com.hellbilling.bambulkacka;

import android.preference.PreferenceActivity;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends PreferenceActivity {

    private static List<String> fragments = new ArrayList<String>();

    @Override
    public void onBuildHeaders(List<Header> target) {

        // Get intent argument for type of header
        String preferencesType = getIntent().getExtras().getString("preferencesType");

        // Run header
        switch (preferencesType){
            case "main" : // Contains all headers
                loadHeadersFromResource(R.xml.preferences_headers_main, target);
                break;
            case "kalkulacka" : // Contains example contains only
                loadHeadersFromResource(R.xml.preferences_headers_kalkulacka, target);
                break;
            default :
                loadHeadersFromResource(R.xml.preferences_headers_main, target);
                break;
        }

        fragments.clear();
        for (Header header : target) {
            fragments.add(header.fragment);
        }

        // This in xml file can give argument into fragment
        // <extra android:name="someKey" android:value="someHeaderValue" />
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return fragments.contains(fragmentName);
    }
}
