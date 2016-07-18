package com.example.emma_baumstarck.todoapp;

import android.view.View;

/**
 * Created by emma_baumstarck on 7/11/16.
 */
public class TodoItem {
    private int id;
    private String value;
    private long createdAtMs;
    private long updatedAtMs;
    private int priority;
    private Integer dueDate;

    public TodoItem(
            int id,
            String value,
            long createdAtMs,
            long updatedAtMs,
            int priority,
            Integer dueDate) {
        this.id = id;
        this.value = value;
        this.createdAtMs = createdAtMs;
        this.updatedAtMs = updatedAtMs;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCreatedAtMs() {
        return createdAtMs;
    }

    public void setCreatedAtMs(long createdAtMs) {
        this.createdAtMs = createdAtMs;
    }

    public long getUpdatedAtMs() {
        return updatedAtMs;
    }

    public void setUpdatedAtMs(long updatedAtMs) {
        this.updatedAtMs = updatedAtMs;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDateDisplay() {
        if (dueDate == null) {
            return "";
        }

        int dueDate = this.dueDate.intValue();
        int month = ((dueDate % 10000) / 100) + 1;
        int day = dueDate % 100;
        return month + "/" + day;
    }

    @Override
    public String toString() {
        return value;
    }
}
