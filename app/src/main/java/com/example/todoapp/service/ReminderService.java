package com.example.todoapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.todoapp.model.Todo;
import com.example.todoapp.receiver.ReminderReceiver;

public class ReminderService {
    public static void scheduleReminder(Context context, Todo todo) {
        if (!todo.isReminderEnabled() || todo.getDueDate() == 0) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("todo_id", todo.getId());
        intent.putExtra("todo_title", todo.getTitle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            todo.getId(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            todo.getDueDate(),
            pendingIntent
        );
    }
} 