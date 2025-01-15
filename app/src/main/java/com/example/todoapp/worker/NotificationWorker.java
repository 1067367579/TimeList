package com.example.todoapp.worker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.util.NotificationHelper;

public class NotificationWorker extends Worker {
    public static final String KEY_TODO_ID = "todo_id";
    public static final String KEY_TODO_TITLE = "todo_title";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString(KEY_TODO_TITLE);
        int todoId = getInputData().getInt(KEY_TODO_ID, 0);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            getApplicationContext(), 
            todoId, 
            intent,
            PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
            getApplicationContext(), 
            NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("待办事项提醒")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), 
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat.from(getApplicationContext())
                    .notify(todoId, builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return Result.success();
    }
} 