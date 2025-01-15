package com.example.todoapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.todoapp.fragment.TodoFragment;

public class TodoPagerAdapter extends FragmentStateAdapter {
    public TodoPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public Fragment createFragment(int position) {
        return TodoFragment.newInstance(position == 1);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
} 