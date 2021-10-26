package com.link.todo.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ToDoDbHelper extends SQLiteOpenHelper {


    public ToDoDbHelper(@Nullable Context context) {
        super(context, ToDoDatabaseContract.DATABASE_NAME, null, ToDoDatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ToDoDatabaseContract.ItemTable.SQL_CREATE_ITEMS_TABLE);
        db.execSQL(ToDoDatabaseContract.PriorityTable.SQL_CREATE_PRIORITY_TABLE);

        for (int i = 0; i < ToDoDatabaseContract.PriorityTable.SQL_CREATE_INITIAL_PRIORITY_DATA.length; i++) {
            db.execSQL(ToDoDatabaseContract.PriorityTable.SQL_CREATE_INITIAL_PRIORITY_DATA[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DELETED);
        db.execSQL(ToDoDatabaseContract.PriorityTable.SQL_DELETE_PRIORITY_TABLE);
        onCreate(db);


    }
}
