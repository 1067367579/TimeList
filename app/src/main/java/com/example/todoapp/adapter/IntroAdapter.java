package com.example.todoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.R;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.IntroViewHolder> {
    private static final int[] IMAGES = new int[] {
        R.drawable.intro_time,
        R.drawable.intro_tag,
        R.drawable.intro_notify
    };

    private static final String[] TITLES = new String[] {
        "高效管理时间",
        "分类整理任务",
        "及时提醒待办"
    };

    private static final String[] DESCRIPTIONS = new String[] {
        "合理规划时间，提高工作效率",
        "使用标签分类，清晰明了",
        "到期自动提醒，不错过任务"
    };

    @NonNull
    @Override
    public IntroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_intro, parent, false);
        return new IntroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    static class IntroViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView descriptionView;

        IntroViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageIntro);
            titleView = itemView.findViewById(R.id.textTitle);
            descriptionView = itemView.findViewById(R.id.textDescription);
        }

        void bind(int position) {
            imageView.setImageResource(IMAGES[position]);
            titleView.setText(TITLES[position]);
            descriptionView.setText(DESCRIPTIONS[position]);
        }
    }
} 