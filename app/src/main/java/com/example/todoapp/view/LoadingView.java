package com.example.todoapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.todoapp.R;

public class LoadingView extends FrameLayout {
    private ImageView imageView;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_loading, this, true);
        imageView = view.findViewById(R.id.loading_image);
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        imageView.startAnimation(rotation);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        imageView.clearAnimation();
    }
} 