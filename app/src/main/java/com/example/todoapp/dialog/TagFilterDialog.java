package com.example.todoapp.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

public class TagFilterDialog extends DialogFragment {
    private OnTagSelectedListener listener;

    public interface OnTagSelectedListener {
        void onTagSelected(String tag);
    }

    public static TagFilterDialog newInstance(List<String> tags) {
        TagFilterDialog dialog = new TagFilterDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("tags", new ArrayList<>(tags));
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnTagSelectedListener(OnTagSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> tags = getArguments().getStringArrayList("tags");
        String[] tagsArray = tags.toArray(new String[0]);

        return new AlertDialog.Builder(requireContext())
            .setTitle("按标签筛选")
            .setItems(tagsArray, (dialog, which) -> {
                if (listener != null) {
                    listener.onTagSelected(tagsArray[which]);
                }
            })
            .setNegativeButton("取消", null)
            .create();
    }
} 