package com.example.todoapp.util;

import android.content.Context;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTimePickerDialog {
    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(long timestamp);
    }

    public static void show(Context context, long currentTimestamp, OnDateTimeSelectedListener listener) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        if (currentTimestamp > 0) {
            calendar.setTimeInMillis(currentTimestamp);
        }

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(calendar.getTimeInMillis())
            .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar selectedCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            selectedCal.setTimeInMillis(selection);

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .build();

            timePicker.addOnPositiveButtonClickListener(v -> {
                selectedCal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                selectedCal.set(Calendar.MINUTE, timePicker.getMinute());
                listener.onDateTimeSelected(selectedCal.getTimeInMillis());
            });

            timePicker.show(((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager(), "time_picker");
        });

        datePicker.show(((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager(), "date_picker");
    }
} 