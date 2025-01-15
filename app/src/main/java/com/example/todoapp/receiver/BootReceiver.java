package com.example.todoapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.todoapp.data.TodoDatabase;
import com.example.todoapp.model.Todo;
import com.example.todoapp.util.NotificationHelper;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                TodoDatabase db = TodoDatabase.getInstance(context);
                List<Todo> todos = db.todoDao().getAllTodosWithReminders();
                for (Todo todo : todos) {
                    if (todo.isReminderEnabled() && !todo.isCompleted() 
                            && todo.getDueDate() > System.currentTimeMillis()) {
                        NotificationHelper.scheduleNotification(context, todo);
                    }
                }
            });
            executor.shutdown();
        }
    }
} 