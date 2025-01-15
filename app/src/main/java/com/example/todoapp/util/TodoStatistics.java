package com.example.todoapp.util;

import com.example.todoapp.model.Todo;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TodoStatistics {
    public static Map<String, Long> getTagStatistics(List<Todo> todos) {
        return todos.stream()
            .collect(Collectors.groupingBy(
                Todo::getTag,
                Collectors.counting()
            ));
    }

    public static long getOverdueTodosCount(List<Todo> todos) {
        return todos.stream()
            .filter(todo -> !todo.isCompleted() && TodoUtils.isOverdue(todo))
            .count();
    }
} 