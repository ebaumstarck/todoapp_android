package com.example.emma_baumstarck.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown
 * Created by emma_baumstarck on 7/12/16.
 */
public class TodoItemArrayAdapter extends ArrayAdapter<TodoItem> {
    private final Context context;
    private final List<TodoItem> todoItems;

    public TodoItemArrayAdapter(Context context, List<TodoItem> todoItems) {
        super(context, -1, todoItems);
        this.context = context;
        this.todoItems = todoItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.todo_item_layout, parent, false);
        TodoItem todoItem = todoItems.get(position);

        TextView leftLabel = (TextView) rowView.findViewById(R.id.leftLabel);
        leftLabel.setText(todoItem.getValue());

        TextView rightLabel = (TextView) rowView.findViewById(R.id.rightLabel);
        int priority = todoItem.getPriority();
        rightLabel.setTypeface(null, Typeface.NORMAL);
        switch (priority) {
            case TodoItemsTableProvider.PRIORITY_LOW:
                rightLabel.setText(R.string.priority_low);
                rightLabel.setTextColor(Color.BLUE);
                break;
            case TodoItemsTableProvider.PRIORITY_REGULAR:
                rightLabel.setText(R.string.priority_regular);
                rightLabel.setTextColor(Color.BLACK);
                break;
            case TodoItemsTableProvider.PRIORITY_HIGH:
                rightLabel.setText(R.string.priority_high);
                rightLabel.setTextColor(Color.RED);
                rightLabel.setTypeface(null, Typeface.BOLD);
                break;
        }

        TextView dueDateLabel = (TextView) rowView.findViewById(R.id.dueDateLabel);
        dueDateLabel.setVisibility(View.VISIBLE);
        dueDateLabel.setText(todoItem.getDueDateDisplay());

        rowView.setTag(todoItem);
        return rowView;
    }
}
