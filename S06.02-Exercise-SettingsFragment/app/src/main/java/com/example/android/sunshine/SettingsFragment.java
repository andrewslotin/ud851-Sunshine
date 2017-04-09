package com.example.android.sunshine;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_screen);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences preferences = preferenceScreen.getSharedPreferences();
        int preferenceCount = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (preference instanceof CheckBoxPreference) {
                continue;
            }

            setPreferenceSummary(preference, preferences.getString(preference.getKey(), ""));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        if (preference instanceof CheckBoxPreference) {
            return;
        }

        String stringValue = (String) value;

        if (!(preference instanceof ListPreference)) {
            preference.setSummary(value.toString());
            return;
        }

        ListPreference listPreference = (ListPreference) preference;
        int valueIndex = listPreference.findIndexOfValue(stringValue);
        if (valueIndex < 0) {
            return;
        }

        listPreference.setSummary(listPreference.getEntries()[valueIndex]);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (null == preference || preference instanceof CheckBoxPreference) {
            return;
        }

        setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), ""));
    }
}
