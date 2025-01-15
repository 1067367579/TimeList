package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.todoapp.adapter.TodoPagerAdapter;
import com.example.todoapp.databinding.ActivityMainBinding;
import com.example.todoapp.dialog.TagFilterDialog;
import com.example.todoapp.model.Todo;
import com.example.todoapp.model.SortType;
import com.example.todoapp.util.TodoUtils;
import com.example.todoapp.viewmodel.TodoViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.List;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import androidx.appcompat.widget.SearchView;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.os.Build;
import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TodoViewModel todoViewModel;
    private static final int ADD_TODO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getBooleanExtra("request_notification", false)) {
            requestNotificationPermission();
        }

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        setSupportActionBar(binding.toolbar);
        setupViewPager();
        setupFab();
    }

    private void setupViewPager() {
        TodoPagerAdapter pagerAdapter = new TodoPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
            (tab, position) -> tab.setText(position == 0 ? "待办" : "已完成")
        ).attach();
    }

    private void setupFab() {
        binding.fabAddTodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
            startActivityForResult(intent, ADD_TODO_REQUEST);
        });
    }

    private void showTagFilterDialog() {
        try {
            List<String> tags = new ArrayList<>();
            tags.add("全部");
            tags.addAll(TodoUtils.DEFAULT_TAGS);
            
            new MaterialAlertDialogBuilder(this)
                .setTitle("按标签筛选")
                .setItems(tags.toArray(new String[0]), (dialog, which) -> {
                    String selectedTag = which == 0 ? null : tags.get(which);
                    todoViewModel.setTagFilter(selectedTag);
                })
                .show();
        } catch (Exception e) {
            Log.e("MainActivity", "显示标签过滤对话框时发生错误", e);
            Snackbar.make(binding.getRoot(), "无法加载标签列表", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showSortDialog() {
        String[] sortOptions = {
            "按优先级（高到低）",
            "按优先级（低到高）",
            "按截止日期（近到远）",
            "按截止日期（远到近）",
            "按创建时间（新到旧）",
            "按创建时间（旧到新）",
            "按标签筛选"
        };

        new MaterialAlertDialogBuilder(this)
            .setTitle("排序与筛选")
            .setItems(sortOptions, (dialog, which) -> {
                if (which == 6) {
                    showTagFilterDialog();
                } else {
                    switch (which) {
                        case 0:
                            todoViewModel.setSortType(SortType.PRIORITY_DESC);
                            break;
                        case 1:
                            todoViewModel.setSortType(SortType.PRIORITY_ASC);
                            break;
                        case 2:
                            todoViewModel.setSortType(SortType.DUE_DATE_ASC);
                            break;
                        case 3:
                            todoViewModel.setSortType(SortType.DUE_DATE_DESC);
                            break;
                        case 4:
                            todoViewModel.setSortType(SortType.CREATED_DATE_DESC);
                            break;
                        case 5:
                            todoViewModel.setSortType(SortType.CREATED_DATE_ASC);
                            break;
                    }
                }
            })
            .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TODO_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                Todo todo = data.getParcelableExtra("todo");
                if (todo != null) {
                    todoViewModel.insert(todo);
                    Snackbar.make(binding.getRoot(), "待办事项已添加", Snackbar.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("MainActivity", "添加待办事项时发生错误", e);
                Snackbar.make(binding.getRoot(), "添加失败，请重试", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                todoViewModel.setSearchQuery(newText);
                return true;
            }
        });
        
        searchView.setQueryHint("搜索待办事项...");
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            showSortDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) 
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    1001
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 如果用户拒绝了权限，引导用户去设置页面开启
                new AlertDialog.Builder(this)
                    .setTitle("需要通知权限")
                    .setMessage("为了及时提醒您待办事项，请允许应用发送通知")
                    .setPositiveButton("去设置", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        startActivity(intent);
                    })
                    .setNegativeButton("取消", null)
                    .show();
            }
        }
    }

    // ... 其他方法
} 