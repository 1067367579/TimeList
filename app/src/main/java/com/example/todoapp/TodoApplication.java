package com.example.todoapp;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.work.Configuration;
import com.example.todoapp.util.NotificationHelper;
import com.example.todoapp.util.TodoPreferences;

public class TodoApplication extends Application implements Configuration.Provider {
    @Override
    public void onCreate() {
        super.onCreate();
        
        TodoPreferences preferences = new TodoPreferences(this);
        if (preferences.isFirstRun()) {
            NotificationHelper.createNotificationChannel(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("request_notification", true);
                startActivity(intent);
            }
            preferences.setFirstRun(false);
        }
    }

    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build();
    }
} 