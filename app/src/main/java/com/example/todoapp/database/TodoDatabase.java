package com.example.todoapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.todoapp.dao.TodoDao;
import com.example.todoapp.model.Todo;
import com.example.todoapp.util.DateConverter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class}, version = 2, exportSchema = true)
@TypeConverters({DateConverter.class})
public abstract class TodoDatabase extends RoomDatabase {
    private static volatile TodoDatabase INSTANCE;
    
    // 创建数据库执行器
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor = 
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    public abstract TodoDao todoDao();
    
    public static TodoDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        TodoDatabase.class,
                        "todo_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
} 