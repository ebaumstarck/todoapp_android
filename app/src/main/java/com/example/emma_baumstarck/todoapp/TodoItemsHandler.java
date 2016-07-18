package com.example.emma_baumstarck.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emma_baumstarck on 7/10/16.
 */
public class TodoItemsHandler {
    private TodoAppDatabase dbHelper;
    private SQLiteDatabase database;
    private static TodoItemsHandler singleton;
    /**
     * cache of all items in memory
     */
    Map<Integer, TodoItem> todoItemsById;

    /**
     * @param context
     */
    public TodoItemsHandler(Context context) {
        dbHelper = new TodoAppDatabase(context);
        database = dbHelper.getWritableDatabase();
        todoItemsById = new HashMap<>();
    }

    public static TodoItemsHandler getInstance() {
        if (singleton == null) {
            return TodoItemsHandler.syncGetInstance();
        }
        return singleton;
    }

    private synchronized static TodoItemsHandler syncGetInstance() {
        if (singleton == null) {
            singleton = new TodoItemsHandler(ApplicationContext.get());
        }
        return singleton;
    }

    public TodoItem addTodo(String value, int priority) {
        ContentValues values = new ContentValues();
        values.put(TodoItemsTableProvider.VALUE_KEY, value);
        long currentTimeMs = System.currentTimeMillis();
        values.put(TodoItemsTableProvider.CREATED_AT_MS_KEY, currentTimeMs);
        values.put(TodoItemsTableProvider.UPDATED_AT_MS_KEY, currentTimeMs);
        values.put(TodoItemsTableProvider.PRIORITY_KEY, priority);

        long id = database.insert(TodoItemsTableProvider.TABLE, null, values);
        TodoItem todoItem = new TodoItem(
                (int) id,
                value,
                currentTimeMs,
                currentTimeMs,
                TodoItemsTableProvider.PRIORITY_REGULAR,
                null);
        todoItemsById.put((int) id, todoItem);
        return todoItem;
    }

    private TodoItem fromCursor(Cursor cursor) {
        TodoItem todoItem = new TodoItem(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getLong(2),
                cursor.getLong(3),
                cursor.getInt(4),
                cursor.isNull(5) ? null : cursor.getInt(5));
        todoItemsById.put(todoItem.getId(), todoItem);
        return todoItem;
    }

    public TodoItem getTodo(long id) {
        return todoItemsById.get((int) id);
    }

    public boolean updateTodo(TodoItem todoItem, String value, int priority, Integer dueDate) {
        ContentValues values = new ContentValues();

        todoItem.setValue(value);
        values.put(TodoItemsTableProvider.VALUE_KEY, value);

        long updatedAtMs = System.currentTimeMillis();
        todoItem.setUpdatedAtMs(updatedAtMs);
        values.put(TodoItemsTableProvider.UPDATED_AT_MS_KEY, updatedAtMs);

        todoItem.setPriority(priority);
        values.put(TodoItemsTableProvider.PRIORITY_KEY, priority);

        todoItem.setDueDate(dueDate);
        values.put(TodoItemsTableProvider.DUE_DATE_KEY, dueDate);

        database.update(
                TodoItemsTableProvider.TABLE,
                values,
                TodoItemsTableProvider.ID_KEY + "=?",
                new String[]{String.valueOf(todoItem.getId())});
        return true;
    }

    public void deleteTodo(TodoItem item) {
        database.execSQL(
                "delete from " + TodoItemsTableProvider.TABLE + " where " +
                        TodoItemsTableProvider.ID_KEY + "=" + item.getId());
        todoItemsById.remove(item.getId());
    }

    public List<TodoItem> getAllTodos() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();
        Cursor cursor = database.rawQuery(
                "select " + TodoItemsTableProvider.ID_KEY + ", " +
                        TodoItemsTableProvider.VALUE_KEY + ", " +
                        TodoItemsTableProvider.CREATED_AT_MS_KEY + ", " +
                        TodoItemsTableProvider.UPDATED_AT_MS_KEY + ", " +
                        TodoItemsTableProvider.PRIORITY_KEY + ", " +
                        TodoItemsTableProvider.DUE_DATE_KEY +
                        " from " + TodoItemsTableProvider.TABLE,
                null);
        if (cursor.moveToFirst()) {
            do {
                todoItems.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return todoItems;
    }
}
