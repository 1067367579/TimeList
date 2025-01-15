package com.example.todoapp.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        View view = holder.itemView;
        view.setAlpha(0f);
        view.setTranslationY(view.getHeight());

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", 
            view.getHeight(), 0f);

        alpha.setDuration(300);
        translateY.setDuration(300);
        translateY.setInterpolator(new AccelerateDecelerateInterpolator());

        alpha.start();
        translateY.start();

        return true;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        View view = holder.itemView;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);

        alpha.setDuration(200);
        scaleX.setDuration(200);
        scaleY.setDuration(200);

        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchRemoveFinished(holder);
            }
        });

        alpha.start();
        scaleX.start();
        scaleY.start();

        return true;
    }
} 