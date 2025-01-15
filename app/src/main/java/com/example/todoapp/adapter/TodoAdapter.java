package com.example.todoapp.adapter;

import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.R;
import com.example.todoapp.databinding.ItemTodoBinding;
import com.example.todoapp.model.Todo;
import java.util.Date;

public class TodoAdapter extends ListAdapter<Todo, TodoAdapter.TodoViewHolder> {
    private final TodoClickListener listener;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Todo todo);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    public TodoAdapter(TodoClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTodoBinding binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new TodoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = getItem(position);
        holder.bind(todo);
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private final ItemTodoBinding binding;

        TodoViewHolder(ItemTodoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.checkboxCompleted.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Todo todo = getItem(position);
                    todo.setCompleted(!todo.isCompleted());
                    listener.onTodoCompleted(todo);
                }
            });

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTodoClicked(getItem(position));
                }
            });

            binding.buttonDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(getItem(getAdapterPosition()));
                }
            });
        }

        void bind(Todo todo) {
            binding.checkboxCompleted.setChecked(todo.isCompleted());
            binding.textTitle.setText(todo.getTitle());
            binding.textDescription.setText(todo.getDescription());
            binding.textDueDate.setText(DateFormat.format("yyyy-MM-dd HH:mm", new Date(todo.getDueDate())));
            
            int textColor = todo.isCompleted() ? 
                Color.GRAY : 
                binding.getRoot().getContext().getResources().getColor(R.color.text_primary);
            binding.textTitle.setTextColor(textColor);
            binding.textDescription.setTextColor(textColor);
            binding.textDueDate.setTextColor(textColor);
            
            binding.priorityIndicator.setBackgroundResource(getPriorityColorResource(todo.getPriority()));
        }

        private int getPriorityColorResource(int priority) {
            switch (priority) {
                case 1: return R.color.priority_high;
                case 2: return R.color.priority_medium;
                default: return R.color.priority_low;
            }
        }
    }

    public interface TodoClickListener {
        void onTodoClicked(Todo todo);
        void onTodoCompleted(Todo todo);
    }

    private static final DiffUtil.ItemCallback<Todo> DIFF_CALLBACK = 
        new DiffUtil.ItemCallback<Todo>() {
            @Override
            public boolean areItemsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
                return oldItem.equals(newItem);
            }
        };
} 