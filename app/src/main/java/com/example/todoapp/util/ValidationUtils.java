package com.example.todoapp.util;

import com.google.android.material.textfield.TextInputLayout;

public class ValidationUtils {
    public static void validateTitle(TextInputLayout layout, String title) {
        if (title.trim().isEmpty()) {
            layout.setError("标题不能为空");
        } else {
            layout.setError(null);
        }
    }

    public static void validateDescription(TextInputLayout layout, String description) {
        if (description.trim().length() > 500) {
            layout.setError("描述不能超过500字");
        } else {
            layout.setError(null);
        }
    }
} 