package com.example.emma_baumstarck.todoapp;

/**
 * Created by emma_baumstarck on 7/16/16.
 */
public interface TableProvider {
    /** get name of table to create */
    public String getTableName();
    /** get list of fields to make. "_id integer primary key", "foo text not null" */
    public String[] getFieldNames();
}
