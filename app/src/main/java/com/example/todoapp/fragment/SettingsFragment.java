package com.example.todoapp.fragment;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.ListPreference;
import androidx.preference.SwitchPreferenceCompat;
import com.example.todoapp.R;
import com.example.todoapp.util.ThemeHelper;
import com.example.todoapp.util.TodoPreferences;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        ListPreference themePreference = findPreference("theme");
        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                ThemeHelper.applyTheme(requireContext());
                return true;
            });
        }

        SwitchPreferenceCompat notificationPreference = findPreference("notification_enabled");
        if (notificationPreference != null) {
            notificationPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                TodoPreferences prefs = new TodoPreferences(requireContext());
                prefs.setNotificationEnabled((Boolean) newValue);
                return true;
            });
        }
    }
} 