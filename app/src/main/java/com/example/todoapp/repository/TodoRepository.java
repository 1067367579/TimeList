package com.example.todoapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.todoapp.data.TodoDao;
import com.example.todoapp.data.TodoDatabase;
import com.example.todoapp.model.Todo;
import java.util.List;

public class TodoRepository {
    private final TodoDao todoDao;
    private final LiveData<List<Todo>> allTodos;
    private final LiveData<List<String>> allTags;

    public TodoRepository(Application application) {
        TodoDatabase database = TodoDatabase.getInstance(application);
        todoDao = database.todoDao();
        allTodos = todoDao.getAllTodos();
        allTags = todoDao.getAllTags();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    public LiveData<List<String>> getAllTags() {
        return allTags;
    }

    public void insert(Todo todo) {
        TodoDatabase.getDatabaseWriteExecutor().execute(() -> {
            todoDao.insert(todo);
        });
    }

    public void update(Todo todo) {
        TodoDatabase.getDatabaseWriteExecutor().execute(() -> {
            todoDao.update(todo);
        });
    }

    public void delete(Todo todo) {
        TodoDatabase.getDatabaseWriteExecutor().execute(() -> {
            todoDao.delete(todo);
        });
    }

    public List<Todo> getAllTodosWithReminders() {
        return todoDao.getAllTodosWithReminders();
    }

    public List<Todo> getAllTodosSync() {
        return todoDao.getAllTodosSync();
    }

    public void insertAll(List<Todo> todos) {
        TodoDatabase.getDatabaseWriteExecutor().execute(() -> {
            todoDao.insertAll(todos);
        });
    }

    public LiveData<Todo> getTodoById(int id) {
        return todoDao.getTodoById(id);
    }

    public LiveData<List<Todo>> getTodosByTag(String tag) {
        return todoDao.getTodosByTag(tag);
    }

    public LiveData<List<Todo>> getTodosByCompletion(boolean isCompleted) {
        return todoDao.getTodosByCompletion(isCompleted);
    }

    public void deleteCompletedTodos() {
        TodoDatabase.getDatabaseWriteExecutor().execute(() -> {
            todoDao.deleteCompletedTodos();
        });
    }
} 