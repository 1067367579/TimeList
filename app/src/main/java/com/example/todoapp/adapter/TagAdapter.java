package com.example.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.R;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private final Context context;
    private final List<String> tags;
    private OnTagClickListener listener;

    public TagAdapter(Context context, List<String> tags) {
        this.context = context;
        this.tags = tags;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.tagText.setText(tag);
        
        // 添加动画效果
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void setOnTagClickListener(OnTagClickListener listener) {
        this.listener = listener;
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagText;

        TagViewHolder(View itemView) {
            super(itemView);
            tagText = itemView.findViewById(R.id.text_tag);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTagClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnTagClickListener {
        void onTagClick(int position);
    }
} 