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

public class MainActivity extends AppCompatActivity implements TodoItemEditer {
    // todo items in sorted order
    ArrayList<TodoItem> todoItems;
    TodoItemArrayAdapter itemsAdapter;
    ListView lvItems;

    // action toolbar and the sort button
    Toolbar toolbar;
    MenuItem menuItemSort;

    // the edit and delete buttons on each row are visible
    boolean rowButtonsVisible = false;

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

        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        itemsAdapter = new TodoItemArrayAdapter(this, this, todoItems, lvItems);
        setupListViewListener();

        updateSortOrder(currentSort, ascending);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void sortTodoItems() {
        itemsAdapter.sortTodoItems(currentSort, ascending);
    }

    private void updateSortOrder(SortOrder sortOrder, boolean ascending) {
        currentSort = sortOrder;
        this.ascending = ascending;
        sortTodoItems();
    }

    public void onAddItem(View view) {
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

    @Override
    public void editTodoItem(TodoItem todoItem) {
        Intent myIntent = new Intent(MainActivity.this, EditActivity.class);
        myIntent.putExtra(EditActivity.TASK_ID_KEY, todoItem.getId());
        MainActivity.this.startActivityForResult(myIntent, 0);
    }

    @Override
    public void deleteTodoItem(TodoItem todoItem) {
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

    @Override
    public void deleteClickHandler(View view) {
        setEditLabelVisibility(false);
        RelativeLayout row = (RelativeLayout)view.getParent();
        final TodoItem todoItem = (TodoItem) row.getTag();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_todo_title) + todoItem.getValue())
                .setMessage(getString(R.string.confirm_delete_todo))
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

    private void rowClickHandler(View view) {
        setEditLabelVisibility(false);
        //get the row the clicked button is in
        RelativeLayout row = (RelativeLayout) view.getParent();
        editTodoItem((TodoItem) row.getTag());
    }

    private void setEditLabelVisibility(boolean visible) {
        rowButtonsVisible = visible;
        itemsAdapter.setEditLabelVisibility(rowButtonsVisible);
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
