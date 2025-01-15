package com.example.todoapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class TodoPreferences {
    private static final String KEY_FIRST_RUN = "first_run";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    private static final String KEY_DEFAULT_PRIORITY = "default_priority";
    private static final String KEY_SORT_TYPE = "sort_type";
    private static final String KEY_THEME = "theme";

    private final SharedPreferences preferences;

    public TodoPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isFirstRun() {
        return preferences.getBoolean(KEY_FIRST_RUN, true);
    }

    public void setFirstRun(boolean firstRun) {
        preferences.edit().putBoolean(KEY_FIRST_RUN, firstRun).apply();
    }

    public boolean isNotificationEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATION_ENABLED, true);
    }

    public void setNotificationEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }

    public int getDefaultPriority() {
        return preferences.getInt(KEY_DEFAULT_PRIORITY, 2);
    }

    public void setDefaultPriority(int priority) {
        preferences.edit().putInt(KEY_DEFAULT_PRIORITY, priority).apply();
    }

    public String getSortType() {
        return preferences.getString(KEY_SORT_TYPE, "CREATED_DATE_DESC");
    }

    public void setSortType(String sortType) {
        preferences.edit().putString(KEY_SORT_TYPE, sortType).apply();
    }

    public String getTheme() {
        return preferences.getString(KEY_THEME, "system");
    }

    public void setTheme(String theme) {
        preferences.edit().putString(KEY_THEME, theme).apply();
    }
} 