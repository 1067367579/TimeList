package com.example.todoapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.todoapp.model.Todo;
import com.example.todoapp.model.SortType;
import com.example.todoapp.repository.TodoRepository;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.ArrayList;
import com.example.todoapp.util.TodoSorter;
import android.text.TextUtils;
import com.example.todoapp.util.NotificationHelper;
import com.example.todoapp.database.TodoDatabase;

public class TodoViewModel extends AndroidViewModel {
    private final TodoRepository repository;
    private final LiveData<List<Todo>> allTodos;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<String> tagFilter = new MutableLiveData<>();
    private final MutableLiveData<SortType> sortType = new MutableLiveData<>(SortType.CREATED_DATE_DESC);
    private final MediatorLiveData<List<Todo>> sortedAndFilteredTodos = new MediatorLiveData<>();

    public TodoViewModel(Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodos = repository.getAllTodos();
        
        sortedAndFilteredTodos.addSource(allTodos, todos -> updateTodos());
        sortedAndFilteredTodos.addSource(searchQuery, query -> updateTodos());
        sortedAndFilteredTodos.addSource(tagFilter, tag -> updateTodos());
        sortedAndFilteredTodos.addSource(sortType, type -> updateTodos());
    }

    public void updateTodo(Todo todo) {
        repository.update(todo);
        if (todo.isReminderEnabled() && !todo.isCompleted()) {
            NotificationHelper.scheduleNotification(getApplication(), todo);
        } else {
            NotificationHelper.cancelNotification(getApplication(), todo.getId());
        }
    }

    public void insert(Todo todo) {
        repository.insert(todo);
        if (todo.isReminderEnabled()) {
            NotificationHelper.scheduleNotification(getApplication(), todo);
        }
    }

    public void delete(Todo todo) {
        repository.delete(todo);
        NotificationHelper.cancelNotification(getApplication(), todo.getId());
    }

    private void updateTodos() {
        List<Todo> todos = allTodos.getValue();
        if (todos != null) {
            // 应用搜索过滤
            String query = searchQuery.getValue();
            if (!TextUtils.isEmpty(query)) {
                todos = todos.stream()
                    .filter(todo -> 
                        todo.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        todo.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                        todo.getTag().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            }
            
            // 应用标签过滤
            String tag = tagFilter.getValue();
            if (!TextUtils.isEmpty(tag)) {
                todos = todos.stream()
                    .filter(todo -> tag.equals(todo.getTag()))
                    .collect(Collectors.toList());
            }

            // 应用排序
            SortType currentSortType = sortType.getValue();
            if (currentSortType != null) {
                todos = TodoSorter.sort(todos, currentSortType);
            }
            
            sortedAndFilteredTodos.setValue(todos);
        }
    }

    // Getters for LiveData
    public LiveData<List<Todo>> getTodos() {
        return sortedAndFilteredTodos;
    }

    public LiveData<Todo> getTodoById(int id) {
        return repository.getTodoById(id);
    }

    // Setters for filters and sort
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void setTagFilter(String tag) {
        tagFilter.setValue(tag);
    }

    public void setSortType(SortType type) {
        sortType.setValue(type);
    }

    public void update(Todo todo) {
        TodoDatabase.getDatabaseWriteExecutor().execute(() -> {
            repository.update(todo);
        });
    }
} 