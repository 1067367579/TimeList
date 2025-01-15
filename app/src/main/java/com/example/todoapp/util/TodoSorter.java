package com.example.todoapp.util;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.SortType;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TodoSorter {
    public static List<Todo> sort(List<Todo> todos, SortType sortType) {
        Comparator<Todo> comparator;
        
        switch (sortType) {
            case PRIORITY_DESC:
                comparator = (t1, t2) -> Integer.compare(t2.getPriority(), t1.getPriority());
                break;
            case PRIORITY_ASC:
                comparator = Comparator.comparingInt(Todo::getPriority);
                break;
            case DUE_DATE_DESC:
                comparator = (t1, t2) -> {
                    if (t1.getDueDate() == 0 && t2.getDueDate() == 0) return 0;
                    if (t1.getDueDate() == 0) return 1;
                    if (t2.getDueDate() == 0) return -1;
                    return Long.compare(t2.getDueDate(), t1.getDueDate());
                };
                break;
            case DUE_DATE_ASC:
                comparator = (t1, t2) -> {
                    if (t1.getDueDate() == 0 && t2.getDueDate() == 0) return 0;
                    if (t1.getDueDate() == 0) return 1;
                    if (t2.getDueDate() == 0) return -1;
                    return Long.compare(t1.getDueDate(), t2.getDueDate());
                };
                break;
            case CREATED_DATE_DESC:
                comparator = (t1, t2) -> Long.compare(t2.getCreatedDate(), t1.getCreatedDate());
                break;
            case CREATED_DATE_ASC:
            default:
                comparator = Comparator.comparingLong(Todo::getCreatedDate);
        }
        
        return todos.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
    }
} 