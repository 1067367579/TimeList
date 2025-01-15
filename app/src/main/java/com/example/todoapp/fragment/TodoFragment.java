package com.example.todoapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todoapp.TodoDetailActivity;
import com.example.todoapp.adapter.TodoAdapter;
import com.example.todoapp.databinding.FragmentTodoBinding;
import com.example.todoapp.viewmodel.TodoViewModel;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.model.Todo;
import java.util.List;
import java.util.stream.Collectors;
import android.view.animation.AnimationUtils;
import com.example.todoapp.util.TodoItemDecoration;
import com.example.todoapp.R;
import android.util.Log;
import java.util.ArrayList;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.view.View;

public class TodoFragment extends Fragment {
    private FragmentTodoBinding binding;
    private TodoAdapter adapter;
    private TodoViewModel todoViewModel;
    private boolean showCompleted;

    public static TodoFragment newInstance(boolean showCompleted) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putBoolean("show_completed", showCompleted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        showCompleted = getArguments().getBoolean("show_completed", false);

        todoViewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);
        
        setupRecyclerView();
        observeTodos();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new TodoAdapter(new TodoAdapter.TodoClickListener() {
            @Override
            public void onTodoClicked(Todo todo) {
                Intent intent = new Intent(requireContext(), TodoDetailActivity.class);
                intent.putExtra(TodoDetailActivity.EXTRA_TODO_ID, todo.getId());
                startActivity(intent);
            }

            @Override
            public void onTodoCompleted(Todo todo) {
                todoViewModel.update(todo);
                String message = todo.isCompleted() ? "已完成" : "已恢复";
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.addItemDecoration(new TodoItemDecoration(requireContext()));
        
        // 添加右滑删除功能
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder,
                                @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Todo todo = adapter.getCurrentList().get(position);
                todoViewModel.delete(todo);
                
                Snackbar.make(binding.getRoot(), "已删除", Snackbar.LENGTH_LONG)
                    .setAction("撤销", v -> todoViewModel.insert(todo))
                    .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  float dX, float dY, int actionState, boolean isCurrentlyActive) {
                
                View itemView = viewHolder.itemView;
                Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete);
                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + iconMargin;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                if (dX > 0) { // 右滑
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = iconLeft + deleteIcon.getIntrinsicWidth();
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    // 设置背景颜色
                    Paint paint = new Paint();
                    paint.setColor(ContextCompat.getColor(requireContext(), R.color.delete_background));
                    c.drawRect(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + dX, itemView.getBottom(), paint);

                    deleteIcon.draw(c);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerView);

        // 设置删除按钮点击监听器
        adapter.setOnDeleteClickListener(todo -> {
            new MaterialAlertDialogBuilder(requireContext())
                .setTitle("删除待办事项")
                .setMessage("确定要删除这个待办事项吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    todoViewModel.delete(todo);
                    Snackbar.make(binding.getRoot(), "已删除", Snackbar.LENGTH_LONG)
                        .setAction("撤销", v -> todoViewModel.insert(todo))
                        .show();
                })
                .setNegativeButton("取消", null)
                .show();
        });

        // 添加动画效果
        binding.recyclerView.setLayoutAnimation(
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down));
    }

    private void observeTodos() {
        try {
            todoViewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
                if (todos != null) {
                    List<Todo> filteredTodos = todos.stream()
                        .filter(todo -> todo.isCompleted() == showCompleted)
                        .collect(Collectors.toList());
                    updateUI(filteredTodos);
                }
            });
        } catch (Exception e) {
            Log.e("TodoFragment", "观察待办事项时发生错误", e);
            updateUI(new ArrayList<>());
        }
    }

    private void updateUI(List<Todo> todos) {
        if (todos.isEmpty()) {
            binding.viewFlipper.setDisplayedChild(1);
        } else {
            binding.viewFlipper.setDisplayedChild(0);
            adapter.submitList(new ArrayList<>(todos));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null;
    }
} 