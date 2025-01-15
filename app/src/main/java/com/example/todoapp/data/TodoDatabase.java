package com.example.todoapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.todoapp.model.Todo;
import com.example.todoapp.util.Converters;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class}, version = 2, exportSchema = true)
@TypeConverters({Converters.class})
public abstract class TodoDatabase extends RoomDatabase {
    private static volatile TodoDatabase INSTANCE;
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
                            "todo_database")
                            .addMigrations(MIGRATION_1_2)
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

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS todo_table_new ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "title TEXT NOT NULL,"
                    + "description TEXT,"
                    + "tag TEXT,"
                    + "priority INTEGER NOT NULL,"
                    + "completed INTEGER NOT NULL,"
                    + "created_date INTEGER NOT NULL,"
                    + "due_date INTEGER NOT NULL,"
                    + "reminder_enabled INTEGER NOT NULL)");
            
            database.execSQL("INSERT INTO todo_table_new "
                    + "(id, title, description, tag, priority, completed, created_date, due_date, reminder_enabled) "
                    + "SELECT id, title, description, tag, priority, completed, created_date, due_date, reminder_enabled "
                    + "FROM todo_table");
            
            database.execSQL("DROP TABLE todo_table");
            
            database.execSQL("ALTER TABLE todo_table_new RENAME TO todo_table");
        }
    };
} 