package com.example.todoapp;

import static java.text.DateFormat.*;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.util.DateTimePickerDialog;
import com.example.todoapp.util.TodoUtils;
import com.google.android.material.textfield.TextInputEditText;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.ArrayAdapter;
import com.example.todoapp.databinding.ActivityAddTodoBinding;
import android.os.Handler;
import android.view.View;
import com.example.todoapp.adapter.TagAdapter;
import java.util.Date;
import android.text.Editable;
import android.text.TextWatcher;
import com.example.todoapp.util.ValidationUtils;
import android.os.Looper;
import com.example.todoapp.model.Todo;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.chip.ChipGroup;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.example.todoapp.util.NotificationHelper;

public class AddTodoActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.todoapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.todoapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_DUE_DATE = "com.example.todoapp.EXTRA_DUE_DATE";

    private long selectedDueDate = 0;
    private static final String[] TAGS = new String[] {
        "工作", "学习", "生活", "娱乐", "其他"
    };

    private int selectedPriority = 1;
    private ActivityAddTodoBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTodoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupViews();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setupViews() {
        setupTagAdapter();

        binding.radioGroupPriority.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_low) {
                selectedPriority = 1;
            } else if (checkedId == R.id.radio_medium) {
                selectedPriority = 2;
            } else if (checkedId == R.id.radio_high) {
                selectedPriority = 3;
            }
        });

        binding.buttonSetDueDate.setOnClickListener(v -> showDateTimePicker());

        binding.buttonSave.setOnClickListener(v -> saveTodo());

        setupTextWatchers();
    }

    private void setupTagAdapter() {
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            TodoUtils.DEFAULT_TAGS
        );
        binding.autoCompleteTag.setAdapter(tagAdapter);
    }

    private void setupTextWatchers() {
        binding.editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValidationUtils.validateTitle(binding.editTitleLayout, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValidationUtils.validateDescription(binding.editDescriptionLayout, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void saveTodo() {
        // 显示加载动画
        binding.buttonSave.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        String title = binding.editTitle.getText().toString().trim();
        String description = binding.editDescription.getText().toString().trim();
        String tag = binding.autoCompleteTag.getText().toString().trim();

        // 表单验证
        if (title.isEmpty()) {
            binding.editTitleLayout.setError("请输入标题");
            binding.buttonSave.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
            return;
        }
        binding.editTitleLayout.setError(null);

        if (tag.isEmpty()) {
            tag = "其他";
        } else if (!TodoUtils.DEFAULT_TAGS.contains(tag)) {
            binding.autoCompleteTagLayout.setError("请选择有效的标签");
            binding.buttonSave.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
            return;
        }
        binding.autoCompleteTagLayout.setError(null);

        // 如果设置了提醒但没有设置截止日期
        if (binding.switchReminder.isChecked() && selectedDueDate == 0) {
            Toast.makeText(this, "请先设置截止日期", Toast.LENGTH_SHORT).show();
            binding.buttonSave.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        // 创建Todo对象
        Todo todo = new Todo(title, description, tag, selectedPriority);
        todo.setDueDate(selectedDueDate);
        todo.setReminderEnabled(binding.switchReminder.isChecked());

        // 检查通知权限
        if (todo.isReminderEnabled() && !hasNotificationPermission()) {
            requestNotificationPermission();
            return;
        }

        // 保存并设置提醒
        new Handler().postDelayed(() -> {
            Intent data = new Intent();
            data.putExtra("todo", todo);
            setResult(RESULT_OK, data);
            
            if (todo.isReminderEnabled()) {
                NotificationHelper.scheduleNotification(this, todo);
            }
            
            finish();
        }, 500);
    }

    private boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, 
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                PERMISSION_REQUEST_CODE);
        }
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("选择日期")
            .setSelection(calendar.getTimeInMillis())
            .build();

        datePicker.addOnPositiveButtonClickListener(selectedDate -> {
            Calendar tempCal = Calendar.getInstance();
            tempCal.setTimeInMillis(selectedDate);
            
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("选择时间")
                .build();

            timePicker.addOnPositiveButtonClickListener(v -> {
                Calendar resultCal = Calendar.getInstance();
                resultCal.setTimeInMillis(selectedDate);
                resultCal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                resultCal.set(Calendar.MINUTE, timePicker.getMinute());
                resultCal.set(Calendar.SECOND, 0);
                resultCal.set(Calendar.MILLISECOND, 0);
                
                selectedDueDate = resultCal.getTimeInMillis();
                
                String dateStr = DateFormat.format("yyyy-MM-dd HH:mm",
                    new Date(selectedDueDate)).toString();
                binding.buttonSetDueDate.setText(String.format("截止日期：%s", dateStr));
            });

            timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }
} 