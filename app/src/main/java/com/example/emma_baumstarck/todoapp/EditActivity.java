package com.example.emma_baumstarck.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class EditActivity extends AppCompatActivity implements DatePickerSelectedListener {
    Toolbar toolbar;

    EditText taskNameText;
    TodoItem todoItem;
    TextView taskCreatedAtView;
    TextView taskUpdatedAtView;

    RadioButton priorityLowRadio;
    RadioButton priorityRegularRadio;
    RadioButton priorityHighRadio;

    // the due date displayed on the edit screen
    Integer dueDate;
    CheckBox dueDateCheckbox;
    Button dueDateButton;

    public static final String TASK_ID_KEY = "taskId";
    public static final String OPERATION_CODE_KEY = "operationCode";

    public static final String DATE_FORMAT = "MM/dd/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar = (Toolbar) findViewById(R.id.editToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Edit Item");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        taskNameText = (EditText) findViewById(R.id.taskNameText);
        taskCreatedAtView = (TextView) findViewById(R.id.taskCreatedAtView);
        taskUpdatedAtView = (TextView) findViewById(R.id.taskUpdatedAtView);

        priorityLowRadio = (RadioButton) findViewById(R.id.priorityLowRadio);
        priorityRegularRadio = (RadioButton) findViewById(R.id.priorityRegularRadio);
        priorityHighRadio = (RadioButton) findViewById(R.id.priorityHighRadio);

        long taskId = getIntent().getExtras().getInt(TASK_ID_KEY);
        todoItem = TodoItemsHandler.getInstance().getTodo(taskId);

        taskNameText.setText(todoItem.getValue());
        taskNameText.setSelection(taskNameText.getText().length());

        taskCreatedAtView.setText(
                new SimpleDateFormat(DATE_FORMAT).format(
                        new java.sql.Timestamp(todoItem.getCreatedAtMs())));
        taskUpdatedAtView.setText(
                new SimpleDateFormat(DATE_FORMAT).format(
                        new java.sql.Timestamp(todoItem.getUpdatedAtMs())));

        switch (todoItem.getPriority()) {
            case TodoItemsTableProvider.PRIORITY_LOW:
                priorityLowRadio.setChecked(true);
                break;
            case TodoItemsTableProvider.PRIORITY_REGULAR:
                priorityRegularRadio.setChecked(true);
                break;
            case TodoItemsTableProvider.PRIORITY_HIGH:
                priorityHighRadio.setChecked(true);
                break;
        }

        dueDate = todoItem.getDueDate();
        dueDateCheckbox = (CheckBox) findViewById(R.id.dueDateCheckBox);
        dueDateButton = (Button) findViewById(R.id.dueDateButton);
        updateDueDateUI();
    }

    private void updateDueDateUI() {
        if (dueDate == null) {
            dueDateCheckbox.setChecked(false);
            dueDateButton.setText(
                    new SimpleDateFormat(DATE_FORMAT).format(
                            new Timestamp(System.currentTimeMillis())));
        } else {
            int year = (dueDate.intValue() / 10000) - 1900;
            int month = (dueDate.intValue() % 10000) / 100;
            int day = dueDate.intValue() % 100;
            dueDateCheckbox.setChecked(true);
            dueDateButton.setText(
                    new SimpleDateFormat(DATE_FORMAT).format(
                            new Timestamp(year, month, day, 0, 0, 0, 0)));
        }
    }

    private Intent setActivityResult(int operationCode) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TASK_ID_KEY, todoItem.getId());
        resultIntent.putExtra(OPERATION_CODE_KEY, operationCode);
        setResult(Activity.RESULT_OK, resultIntent);
        return resultIntent;
    }

    public void onCancelButton(View view) {
        finish();
    }

    public void onUpdateButton(View view) {
        int priority;
        if (priorityLowRadio.isChecked()) {
            priority = TodoItemsTableProvider.PRIORITY_LOW;
        } else if (priorityHighRadio.isChecked()) {
            priority = TodoItemsTableProvider.PRIORITY_HIGH;
        } else {
            priority = TodoItemsTableProvider.PRIORITY_REGULAR;
        }

        Integer updatedDueDate = null;
        if (dueDateCheckbox.isChecked()) {
            updatedDueDate = dueDate.intValue();
        }

        TodoItemsHandler.getInstance().updateTodo(
                todoItem,
                taskNameText.getText().toString(),
                priority,
                updatedDueDate);
        finish();
    }

    public void onDueDateClick(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dueDateKey", dueDate == null ? -1 : dueDate.intValue());
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSelected(int year, int month, int day) {
        System.out.println("ZOO: " + year + ", " + month + ", " + day);
        dueDate = year * 10000 + month * 100 + day;
        dueDateCheckbox.setChecked(true);
        updateDueDateUI();
    }
}
