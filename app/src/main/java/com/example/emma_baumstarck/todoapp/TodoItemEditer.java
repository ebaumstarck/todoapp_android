package com.example.emma_baumstarck.todoapp;

import android.view.View;

/**
 * Created by emma_baumstarck on 7/18/16.
 */
public interface TodoItemEditer {
    void editTodoItem(TodoItem todoItem);

    void deleteTodoItem(TodoItem todoItem);

    void deleteClickHandler(View view);
}
