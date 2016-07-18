package com.example.emma_baumstarck.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emma_baumstarck on 7/10/16.
 */
public class TodoAppDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TodoApp1";
    public static final int DATABASE_VERSION = 3;
    private static final TableProvider[] tableProviders = new TableProvider[]{
            new TodoItemsTableProvider(),
    };

    public TodoAppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (TableProvider tableProvider : tableProviders) {
            StringBuilder builder = new StringBuilder();
            builder.append("create table ");
            builder.append(tableProvider.getTableName());

            builder.append(" (");
            int index = 0;
            for (String fieldName : tableProvider.getFieldNames()) {
                if (index > 0) {
                    builder.append(", ");
                }
                builder.append(fieldName);
                index += 1;
            }
            builder.append(");");
            db.execSQL(builder.toString());
        }
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (TableProvider tableProvider : tableProviders) {
            db.execSQL("drop table if exists " + tableProvider.getTableName());
        }
        onCreate(db);
    }
}
