package com.example.todoapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.todoapp.model.Todo;
import java.util.List;

@Dao
public interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("SELECT * FROM todo_table ORDER BY " +
           "CASE WHEN dueDate > 0 AND dueDate < strftime('%s', 'now') * 1000 AND completed = 0 THEN 1 " +
           "ELSE 2 END, " +
           "priority DESC, " +
           "CASE WHEN dueDate > 0 THEN dueDate ELSE 9999999999999 END")
    LiveData<List<Todo>> getAllTodos();

    @Query("SELECT * FROM todo_table WHERE id = :todoId")
    LiveData<Todo> getTodoById(int todoId);

    @Query("SELECT * FROM todo_table ORDER BY priority DESC, dueDate ASC")
    List<Todo> getAllTodosSync();

    @Query("SELECT DISTINCT tag FROM todo_table WHERE tag IS NOT NULL")
    List<String> getAllTags();

    @Query("SELECT * FROM todo_table WHERE completed = :isCompleted")
    LiveData<List<Todo>> getTodosByCompletion(boolean isCompleted);

    @Query("SELECT * FROM todo_table WHERE tag = :tag")
    LiveData<List<Todo>> getTodosByTag(String tag);
} 