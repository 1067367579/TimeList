package com.example.todoapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "todo_table")
public class Todo implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "completed")
    private boolean completed;
    
    @ColumnInfo(name = "created_date")
    private long createdDate;
    
    @ColumnInfo(name = "due_date")
    private long dueDate;
    
    @ColumnInfo(name = "reminder_enabled")
    private boolean reminderEnabled;

    public Todo(String title, String description, String tag, int priority) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.priority = priority;
        this.completed = false;
        this.createdDate = System.currentTimeMillis();
        this.dueDate = 0;
        this.reminderEnabled = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public long getCreatedDate() { return createdDate; }
    public void setCreatedDate(long createdDate) { this.createdDate = createdDate; }
    
    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }
    
    public boolean isReminderEnabled() { return reminderEnabled; }
    public void setReminderEnabled(boolean reminderEnabled) { this.reminderEnabled = reminderEnabled; }

    protected Todo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        tag = in.readString();
        priority = in.readInt();
        completed = in.readByte() != 0;
        createdDate = in.readLong();
        dueDate = in.readLong();
        reminderEnabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(tag);
        dest.writeInt(priority);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeLong(createdDate);
        dest.writeLong(dueDate);
        dest.writeByte((byte) (reminderEnabled ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };
} 