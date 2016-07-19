package com.example.emma_baumstarck.todoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.renderscript.RenderScript;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * http://www.vogella.com/tutorials/AndroidListView/article.html#adapterown
 * Created by emma_baumstarck on 7/12/16.
 */
public class TodoItemArrayAdapter extends ArrayAdapter<TodoItem> {
    private final Context context;
    private final List<TodoItem> todoItems;
    private final TodoItemEditer todoItemEditer;
    private final ListView lvItems;

    public TodoItemArrayAdapter(
            TodoItemEditer todoItemEditer,
            Context context,
            List<TodoItem> todoItems,
            ListView lvItems) {
        super(context, -1, todoItems);
        this.context = context;
        this.todoItems = todoItems;
        this.todoItemEditer = todoItemEditer;
        this.lvItems = lvItems;
        this.lvItems.setAdapter(this);
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

    public void sortTodoItems(SortOrder sortOrder, final boolean ascending) {
        switch (sortOrder) {
            case ALPHA:
            default:
                Collections.sort(
                        todoItems,
                        new Comparator<TodoItem>() {
                            @Override
                            public int compare(TodoItem lhs, TodoItem rhs) {
                                if (ascending) {
                                    return lhs.getValue().toLowerCase().compareTo(rhs.getValue().toLowerCase());
                                } else {
                                    return rhs.getValue().toLowerCase().compareTo(lhs.getValue().toLowerCase());
                                }
                            }
                        }
                );
                break;
            case CREATED_AT:
                Collections.sort(
                        todoItems,
                        new Comparator<TodoItem>() {
                            @Override
                            public int compare(TodoItem lhs, TodoItem rhs) {
                                long lhsCreatedAt = lhs.getCreatedAtMs();
                                long rhsCreatedAt = rhs.getCreatedAtMs();
                                if (lhsCreatedAt == rhsCreatedAt) {
                                    return 0;
                                } else if (lhsCreatedAt < rhsCreatedAt) {
                                    if (ascending) {
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                } else {
                                    if (ascending) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }
                            }
                        }
                );
                break;
            case UPDATED_AT:
                Collections.sort(
                        todoItems,
                        new Comparator<TodoItem>() {
                            @Override
                            public int compare(TodoItem lhs, TodoItem rhs) {
                                long lhsCreatedAt = lhs.getUpdatedAtMs();
                                long rhsCreatedAt = rhs.getUpdatedAtMs();
                                if (lhsCreatedAt == rhsCreatedAt) {
                                    return 0;
                                } else if (lhsCreatedAt < rhsCreatedAt) {
                                    if (ascending) {
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                } else {
                                    if (ascending) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }
                            }
                        }
                );
                break;
            case PRIORITY:
                Collections.sort(
                        todoItems,
                        new Comparator<TodoItem>() {
                            @Override
                            public int compare(TodoItem lhs, TodoItem rhs) {
                                long lhsPriority = lhs.getPriority();
                                long rhsPriority = rhs.getPriority();
                                if (lhsPriority == rhsPriority) {
                                    return 0;
                                } else if (lhsPriority < rhsPriority) {
                                    if (ascending) {
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                } else {
                                    if (ascending) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }
                            }
                        }
                );
                break;
        }
        notifyDataSetChanged();
    }

    private void rowClickHandler(View view) {
        setEditLabelVisibility(false);
        //get the row the clicked button is in
        RelativeLayout row = (RelativeLayout) view.getParent();
        todoItemEditer.editTodoItem((TodoItem) row.getTag());
    }

    public void setEditLabelVisibility(boolean visible) {
        final long animationDurationMs = 400;
        Animation animFadeOut = AnimationUtils.loadAnimation(
                context.getApplicationContext(), android.R.anim.fade_out);
        animFadeOut.setDuration(animationDurationMs);
        Animation animFadeIn = AnimationUtils.loadAnimation(
                context.getApplicationContext(), android.R.anim.fade_in);
        animFadeIn.setDuration(animationDurationMs);
        animFadeIn.setStartOffset(animationDurationMs);

        for (int i = 0; i < lvItems.getChildCount(); i++) {
            RelativeLayout row = (RelativeLayout) lvItems.getChildAt(i);
            TextView editText = (TextView) row.findViewById(R.id.editLabel);
            TextView deleteText = (TextView) row.findViewById(R.id.deleteLabel);
            TextView dueDateText = (TextView) row.findViewById(R.id.dueDateLabel);
            TextView priorityText = (TextView) row.findViewById(R.id.rightLabel);

            if (!visible) {
                // TODO: group edit and delete together to hide and show in one step
                // fade out edit and delete
                editText.setAnimation(animFadeOut);
                editText.setVisibility(View.INVISIBLE);
                deleteText.setAnimation(animFadeOut);
                deleteText.setVisibility(View.INVISIBLE);

                // fade in due date and priority
                TodoItem todoItem = (TodoItem) row.getTag();
                if (todoItem.getDueDate() != null) {
                    dueDateText.setText(todoItem.getDueDateDisplay());
                    dueDateText.setAnimation(animFadeIn);
                    dueDateText.setVisibility(View.VISIBLE);
                }
                priorityText.setAnimation(animFadeIn);
                priorityText.setVisibility(View.VISIBLE);
            } else {
                // fade out due date and priority
                if (dueDateText.getVisibility() == View.VISIBLE) {
                    dueDateText.setAnimation(animFadeOut);
                    dueDateText.setVisibility(View.INVISIBLE);
                }
                priorityText.setAnimation(animFadeOut);
                priorityText.setVisibility(View.INVISIBLE);

                // fade in edit and delete
                editText.setAnimation(animFadeIn);
                editText.setVisibility(View.VISIBLE);
                editText.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rowClickHandler(v);
                            }
                        }
                );
                deleteText.setAnimation(animFadeIn);
                deleteText.setVisibility(View.VISIBLE);
                deleteText.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                todoItemEditer.deleteClickHandler(v);
                            }
                        }
                );
            }
        }
    }
}
