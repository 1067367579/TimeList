package com.example.todoapp.util;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.todoapp.model.Todo;
import com.example.todoapp.repository.TodoRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackupHelper {
    private static final String BACKUP_FILE_NAME = "时刻清单备份_%s.json";
    private final Context context;
    private final TodoRepository repository;

    public BackupHelper(Context context, TodoRepository repository) {
        this.context = context;
        this.repository = repository;
    }

    public void exportData(Uri uri) throws IOException {
        List<Todo> todos = repository.getAllTodosSync();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonData = gson.toJson(todos);

        try (OutputStream os = context.getContentResolver().openOutputStream(uri)) {
            if (os != null) {
                os.write(jsonData.getBytes());
                Toast.makeText(context, "备份成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void importData(Uri uri) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            Gson gson = new Gson();
            Type type = new TypeToken<List<Todo>>(){}.getType();
            List<Todo> todos = gson.fromJson(reader, type);
            
            if (todos != null && !todos.isEmpty()) {
                repository.insertAll(todos);
                Toast.makeText(context, "导入成功", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "导入失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw e;
        }
    }

    public static void exportTodos(Context context, Uri uri) {
        TodoRepository repository = new TodoRepository(((Application) context.getApplicationContext()));
        List<Todo> todos = repository.getAllTodosSync();
        
        // 按创建时间排序
        todos.sort((a, b) -> Long.compare(b.getCreatedDate(), a.getCreatedDate()));
        // ... 导出逻辑
    }

    public static void importTodos(Context context, Uri uri) {
        TodoRepository repository = new TodoRepository(((Application) context.getApplicationContext()));
        // ... 其他代码
    }
} 