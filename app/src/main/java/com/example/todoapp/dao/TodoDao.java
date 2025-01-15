package com.example.todoapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.todoapp.model.Todo;
import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY created_date DESC")
    LiveData<List<Todo>> getAllTodos();

    @Query("SELECT * FROM todo_table")
    List<Todo> getAllTodosSync();

    @Query("SELECT * FROM todo_table WHERE completed = :isCompleted")
    LiveData<List<Todo>> getTodosByCompletion(boolean isCompleted);

    @Query("SELECT * FROM todo_table WHERE tag = :tag")
    LiveData<List<Todo>> getTodosByTag(String tag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Todo todo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Todo> todos);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("SELECT * FROM todo_table WHERE id = :id")
    LiveData<Todo> getTodoById(int id);

    @Query("SELECT DISTINCT tag FROM todo_table WHERE tag IS NOT NULL")
    List<String> getAllTags();
} 