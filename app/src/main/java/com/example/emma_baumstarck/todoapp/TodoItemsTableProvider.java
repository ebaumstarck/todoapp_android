package com.example.emma_baumstarck.todoapp;

/**
 * builds table for todo items
 * Created by emma_baumstarck on 7/16/16.
 */
public class TodoItemsTableProvider implements TableProvider {
    public static final String TABLE = "TodoItems";
    public static final String ID_KEY = "_id";
    public static final String VALUE_KEY = "value";
    public static final String CREATED_AT_MS_KEY = "created_at_ms";
    public static final String UPDATED_AT_MS_KEY = "updated_at_ms";
    public static final String PRIORITY_KEY = "priority";
    public static final String DUE_DATE_KEY = "due_date";

    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_REGULAR = 0;
    public static final int PRIORITY_HIGH = 1;

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{
                ID_KEY + " integer primary key",
                VALUE_KEY + " text not null",
                CREATED_AT_MS_KEY + " integer not null",
                UPDATED_AT_MS_KEY + " integer not null",
                PRIORITY_KEY + " integer not null",
                DUE_DATE_KEY + " integer null",
        };
    }
}
