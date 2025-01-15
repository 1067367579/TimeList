package com.example.todoapp.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.todoapp.model.Todo;
import com.example.todoapp.worker.NotificationWorker;
import java.util.concurrent.TimeUnit;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    public static final String CHANNEL_ID = "todo_notification";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "待办事项提醒",
                NotificationManager.IMPORTANCE_HIGH
            );
            
            // 配置通知渠道
            channel.setDescription("待办事项到期提醒");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);
            
            // 设置声音
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

            NotificationManager notificationManager = 
                context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void scheduleNotification(Context context, Todo todo) {
        if (!todo.isReminderEnabled() || todo.getDueDate() <= System.currentTimeMillis()) {
            return;
        }

        Data inputData = new Data.Builder()
            .putInt(NotificationWorker.KEY_TODO_ID, todo.getId())
            .putString(NotificationWorker.KEY_TODO_TITLE, todo.getTitle())
            .build();

        long delayInMillis = todo.getDueDate() - System.currentTimeMillis();
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotificationWorker.class)
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag("todo_notification_" + todo.getId())
            .build();

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "todo_notification_" + todo.getId(),
                ExistingWorkPolicy.REPLACE,
                notificationWork
            );
    }

    public static void cancelNotification(Context context, int todoId) {
        // 取消WorkManager中的通知任务
        WorkManager.getInstance(context)
            .cancelUniqueWork("todo_notification_" + todoId);
        
        // 取消已经发出的通知
        NotificationManagerCompat.from(context).cancel(todoId);
    }
} 