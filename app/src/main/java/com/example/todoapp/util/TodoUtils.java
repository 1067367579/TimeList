package com.example.todoapp.util;

import com.example.todoapp.R;
import com.example.todoapp.model.Todo;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;

public class TodoUtils {
    public static final List<String> DEFAULT_TAGS = Arrays.asList(
        "工作", "学习", "生活", "娱乐", "其他"
    );

    public static int getTagColorResource(String tag) {
        switch (tag) {
            case "工作":
                return R.color.tag_work;
            case "学习":
                return R.color.tag_study;
            case "生活":
                return R.color.tag_life;
            case "娱乐":
                return R.color.tag_entertainment;
            default:
                return R.color.tag_other;
        }
    }

    public static int getPriorityColorResource(int priority) {
        switch (priority) {
            case 3:
                return R.color.priority_high;
            case 2:
                return R.color.priority_medium;
            default:
                return R.color.priority_low;
        }
    }

    public static boolean isOverdue(Todo todo) {
        if (todo.getDueDate() <= 0 || todo.isCompleted()) {
            return false;
        }
        return System.currentTimeMillis() > todo.getDueDate();
    }

    public static String getFormattedDueDate(long dueDate) {
        if (dueDate <= 0) {
            return "无截止日期";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dueDate);
        return android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", cal).toString();
    }

    public static String getPriorityText(int priority) {
        switch (priority) {
            case 3:
                return "高优先级";
            case 2:
                return "中优先级";
            default:
                return "低优先级";
        }
    }
} 