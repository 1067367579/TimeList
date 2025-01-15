package com.example.todoapp.data;

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

    @Query("SELECT * FROM todo_table ORDER BY created_date DESC")
    LiveData<List<Todo>> getAllTodos();

    @Query("SELECT * FROM todo_table WHERE reminder_enabled = 1 AND completed = 0")
    List<Todo> getAllTodosWithReminders();

    @Query("SELECT * FROM todo_table")
    List<Todo> getAllTodosSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Todo> todos);

    @Query("SELECT * FROM todo_table WHERE id = :id")
    LiveData<Todo> getTodoById(int id);

    @Query("SELECT DISTINCT tag FROM todo_table WHERE tag IS NOT NULL")
    LiveData<List<String>> getAllTags();

    @Query("DELETE FROM todo_table WHERE completed = 1")
    void deleteCompletedTodos();

    @Query("SELECT * FROM todo_table WHERE tag = :tag")
    LiveData<List<Todo>> getTodosByTag(String tag);

    @Query("SELECT * FROM todo_table WHERE completed = :isCompleted")
    LiveData<List<Todo>> getTodosByCompletion(boolean isCompleted);
} 