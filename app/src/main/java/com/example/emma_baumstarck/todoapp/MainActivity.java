package com.example.emma_baumstarck.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    // todo items in sorted order
    ArrayList<TodoItem> todoItems;
    TodoItemArrayAdapter itemsAdapter;
    ListView lvItems;
//    private static final int EDIT_ACTIVITY_CODE = 1;

    // action toolbar and the sort button
    Toolbar toolbar;
    MenuItem menuItemSort;

    // the edit and delete buttons on each row are visible
    boolean rowButtonsVisible = false;

    private enum SortOrder {
        ALPHA,
        CREATED_AT,
        UPDATED_AT,
        PRIORITY,
    }
    // the order todo items are sorted in
    private SortOrder currentSort;
    private boolean ascending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationContext.getInstance().init(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.todo_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        menuItemSort = (MenuItem) findViewById(R.id.miSort);

        currentSort = SortOrder.ALPHA;
        ascending = true;

        lvItems = (ListView)findViewById(R.id.lvItems);

        readItems();
        itemsAdapter = new TodoItemArrayAdapter(this, todoItems);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

        updateSortOrder(currentSort, ascending);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void sortTodoItems() {
        switch (currentSort) {
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
        itemsAdapter.notifyDataSetChanged();
    }

    private void updateSortOrder(SortOrder sortOrder, boolean ascending) {
        currentSort = sortOrder;
        this.ascending = ascending;
        sortTodoItems();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (itemText.length() == 0) {
            return;
        }

        TodoItem todoItem = TodoItemsHandler.getInstance().addTodo(
                itemText,
                TodoItemsTableProvider.PRIORITY_REGULAR);
        todoItems.add(todoItem);
        sortTodoItems();
        itemsAdapter.notifyDataSetChanged();
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                TodoItem todoItem = todoItems.get(pos);
                editTodoItem(todoItem);
                return true;
            }
        });
    }

    private void readItems() {
        todoItems = new ArrayList<TodoItem>();
        for (TodoItem todoItem : TodoItemsHandler.getInstance().getAllTodos()) {
            todoItems.add(todoItem);
        }
    }

    private void editTodoItem(TodoItem todoItem) {
        Intent myIntent = new Intent(MainActivity.this, EditActivity.class);
        myIntent.putExtra(EditActivity.TASK_ID_KEY, todoItem.getId());
        MainActivity.this.startActivityForResult(myIntent, 0);
    }

    private void rowClickHandler(View view) {
        setEditLabelVisibility(false);
        //get the row the clicked button is in
        RelativeLayout row = (RelativeLayout) view.getParent();
        editTodoItem((TodoItem) row.getTag());
    }

    private void deleteTodoItem(TodoItem todoItem) {
        TodoItemsHandler.getInstance().deleteTodo(todoItem);
        int taskId = todoItem.getId();
        for (int i = 0; i < todoItems.size(); i++) {
            if (todoItems.get(i).getId() == taskId) {
                todoItems.remove(i);
                break;
            }
        }
        itemsAdapter.notifyDataSetChanged();
    }

    private void deleteClickHandler(View view) {
        setEditLabelVisibility(false);
        RelativeLayout row = (RelativeLayout)view.getParent();
        final TodoItem todoItem = (TodoItem) row.getTag();
        new AlertDialog.Builder(this)
                .setTitle("Delete: " + todoItem.getValue())
                .setMessage("Do you really want to delete this item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteTodoItem(todoItem);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sortTodoItems();
        itemsAdapter.notifyDataSetChanged();
    }

    private void handleSortClick(SortOrder newSortOrder) {
        if (newSortOrder == currentSort) {
            // flip direction
            ascending ^= true;
        }
        updateSortOrder(newSortOrder, ascending);
    }

    private void setEditLabelVisibility(boolean visible) {
        rowButtonsVisible = visible;

        final long animationDurationMs = 400;
        Animation animFadeOut = AnimationUtils.loadAnimation(
                getApplicationContext(), android.R.anim.fade_out);
        animFadeOut.setDuration(animationDurationMs);
        Animation animFadeIn = AnimationUtils.loadAnimation(
                getApplicationContext(), android.R.anim.fade_in);
        animFadeIn.setDuration(animationDurationMs);
        animFadeIn.setStartOffset(animationDurationMs);


        for (int i = 0; i < lvItems.getChildCount(); i++) {
            RelativeLayout row = (RelativeLayout) lvItems.getChildAt(i);
            TextView editText = (TextView) row.findViewById(R.id.editLabel);
            TextView deleteText = (TextView) row.findViewById(R.id.deleteLabel);
            TextView dueDateText= (TextView) row.findViewById(R.id.dueDateLabel);
            TextView priorityText = (TextView) row.findViewById(R.id.rightLabel);

            if (!visible) {
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
                                deleteClickHandler(v);
                            }
                        }
                );
            }
        }
    }

    public void onEditAction(MenuItem menuItem) {
        setEditLabelVisibility(rowButtonsVisible ^ true);
    }

    public void onSortAction(MenuItem menuItem) {
        PopupMenu popup = new PopupMenu(MainActivity.this, toolbar);
        popup.getMenuInflater().inflate(R.menu.sort_options, popup.getMenu());
        popup.setGravity(Gravity.RIGHT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sortAtoZ:
                    default:
                        updateSortOrder(SortOrder.ALPHA, true);
                        break;
                    case R.id.sortZtoA:
                        updateSortOrder(SortOrder.ALPHA, false);
                        break;
                    case R.id.sortNewest:
                        updateSortOrder(SortOrder.CREATED_AT, false);
                        break;
                    case R.id.sortOldest:
                        updateSortOrder(SortOrder.CREATED_AT, true);
                        break;
                    case R.id.sortImportant:
                        updateSortOrder(SortOrder.PRIORITY, false);
                        break;
                    case R.id.sortUnimportant:
                        updateSortOrder(SortOrder.PRIORITY, true);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}
