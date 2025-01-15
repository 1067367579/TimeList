package com.example.todoapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.todoapp.databinding.ActivityTodoDetailBinding;
import com.example.todoapp.util.TodoUtils;
import com.example.todoapp.viewmodel.TodoViewModel;
import java.util.Calendar;

public class TodoDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TODO_ID = "todo_id";
    private ActivityTodoDetailBinding binding;
    private TodoViewModel todoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("待办详情");

        loadTodoDetails();
    }

    private void loadTodoDetails() {
        int todoId = getIntent().getIntExtra(EXTRA_TODO_ID, -1);
        if (todoId != -1) {
            todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
            todoViewModel.getTodoById(todoId).observe(this, todo -> {
                if (todo != null) {
                    binding.editTitle.setText(todo.getTitle());
                    binding.editDescription.setText(todo.getDescription());
                    
                    if (todo.getTag() != null) {
                        binding.chipTag.setText(todo.getTag());
                        binding.chipTag.setChipBackgroundColorResource(TodoUtils.getTagColorResource(todo.getTag()));
                    } else {
                        binding.chipTag.setVisibility(View.GONE);
                    }
                    
                    if (todo.getDueDate() > 0) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(todo.getDueDate());
                        String dueDate = DateFormat.format("yyyy-MM-dd HH:mm", cal).toString();
                        binding.textDueDate.setText("截止日期：" + dueDate);
                    } else {
                        binding.textDueDate.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
} 