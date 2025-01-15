package com.example.todoapp.util;

import android.text.format.DateFormat;
import java.util.Date;

public class DateUtils {
    public static String formatDate(long timestamp) {
        if (timestamp == 0) return "";
        return DateFormat.format("yyyy-MM-dd HH:mm", new Date(timestamp)).toString();
    }

    public static String getRelativeTimeSpan(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = timestamp - now;
        
        if (diff < 0) {
            return "已过期";
        }
        
        long days = diff / (24 * 60 * 60 * 1000);
        if (days > 0) {
            return days + "天后";
        }
        
        long hours = diff / (60 * 60 * 1000);
        if (hours > 0) {
            return hours + "小时后";
        }
        
        long minutes = diff / (60 * 1000);
        return minutes + "分钟后";
    }
} 