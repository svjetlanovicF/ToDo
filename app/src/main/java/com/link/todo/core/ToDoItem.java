package com.link.todo.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoItem implements Parcelable {

    private Context context;
    private ToDoDbHelper toDoDbHelper;
    private SQLiteDatabase db;

    private long id;
    private String title;
    private String content;
    private long date;
    private boolean completed;
    private boolean deleted;
    private ToDoPriority toDoPriority;


    protected ToDoItem(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        date = in.readLong();
        completed = in.readByte() != 0;
        deleted = in.readByte() != 0;
        toDoPriority = in.readParcelable(ToDoPriority.class.getClassLoader());
        validationMessage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(date);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeParcelable(toDoPriority, flags);
        dest.writeString(validationMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ToDoItem> CREATOR = new Creator<ToDoItem>() {
        @Override
        public ToDoItem createFromParcel(Parcel in) {
            return new ToDoItem(in);
        }

        @Override
        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getCompleted() {
        return completed;
    }

    public ToDoPriority getToDoPriority() {
        return toDoPriority;
    }

    public void setToDoPriority(ToDoPriority toDoPriority) {
        this.toDoPriority = toDoPriority;
    }

    public void setContext(Context context) {
        this.context = context;
        this.toDoPriority.setContext(context);
    }

    public String getDay(){
        Date date = new Date(this.date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public String getMonth(){
        Date date = new Date(this.date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new SimpleDateFormat("MMM").format(cal.getTime());
    }

    public String getYear(){
        Date date = new Date(this.date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public ToDoItem(Context context) {
        this.context = context;
        this.date = System.currentTimeMillis();
        this.completed = false;
        this.deleted = false;
    }

    public ToDoItem(long id, String title, String content, long date, boolean completed, boolean deleted, ToDoPriority toDoPriority, Context context) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.completed = completed;
        this.deleted = deleted;
        this.context = context;
        this.toDoPriority = toDoPriority;

    }

    public boolean insert(){

        boolean succes = false;

        if(toDoDbHelper == null){
            toDoDbHelper = new ToDoDbHelper(this.context);
        }

        try{
            ContentValues values = new ContentValues();

            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_TITLE, title);
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_CONTENT, content);
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DATE, date);
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_COMPLETED, completed);
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DELETED, deleted);
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_PRIORITY, toDoPriority.getId());

            long rowId = db.insert(ToDoDatabaseContract.ItemTable.TABLE_NAME, null, values);

            if(rowId != -1) {
                succes = true;
                this.id = rowId;
            }
        }
        catch (Exception exc){
            Log.i("Exception - Update", exc.toString());
        }
        finally{
            db.close();
        }

        return succes;
    }

    public boolean update() {

        boolean success = false;

        if (toDoDbHelper == null) {
            toDoDbHelper = new ToDoDbHelper(context);
        }

        db = toDoDbHelper.getWritableDatabase();

        try {
            // Create a new map of updated values
            ContentValues values = new ContentValues();
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_TITLE, title);
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_CONTENT, content);
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_PRIORITY, toDoPriority.getId());

            int rowCount = db.update(ToDoDatabaseContract.ItemTable.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(id)});

            if(rowCount == 1){
                success = true;
            }

        } catch (Exception exc) {
            Log.e("Exception - Update", exc.toString());
        } finally {
            db.close();
        }

        return success;
    }

    public boolean markCompleted() {

        boolean success = false;

        if (toDoDbHelper == null) {
            toDoDbHelper = new ToDoDbHelper(context);
        }

        db = toDoDbHelper.getWritableDatabase();

        try {
            // Create a new map of updated values
            ContentValues values = new ContentValues();
            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_COMPLETED, 1);

            int rowCount = db.update(ToDoDatabaseContract.ItemTable.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(id)});

            if(rowCount == 1){
                success = true;
            }

        } catch (Exception exc) {
            Log.e("Exception - markComp", exc.toString());
        } finally {
            db.close();
        }

        return success;
    }

    public boolean delete() {

        boolean success = false;

        if (toDoDbHelper == null) {
            toDoDbHelper = new ToDoDbHelper(context);
        }

        try {

            db = toDoDbHelper.getWritableDatabase();

            // Create a new map of updated values
            ContentValues values = new ContentValues();

            values.put(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DELETED, 1);
            int rowCount = db.update(ToDoDatabaseContract.ItemTable.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(id)});

            if(rowCount > 0){
                success = true;
            }

        } catch (Exception exc) {
            Log.e("Exception - Delete", exc.toString());
        } finally {
            db.close();
        }

        return success;
    }


    public static ToDoItem[] readAllActive(Context context) {
    ToDoItem[] items = null;

    ToDoDbHelper toDoDbHelper = new ToDoDbHelper(context);
    SQLiteDatabase db = toDoDbHelper.getReadableDatabase();

    ToDoItem toDoItem = null;
    Cursor cursor = null;

    try {

        cursor = db.rawQuery("SELECT item._id AS item_id, item.title, item.content, item.date, item.completed, item.deleted, priority.name, priority._id AS priority_id, priority.color FROM item JOIN priority ON item.priority = priority._id WHERE completed = ? AND deleted = ? ORDER BY item_id DESC", new String[]{"0", "0"});
        items = new ToDoItem[cursor.getCount()];
        int counter = 0;

        while (cursor.moveToNext()) {

            int _id = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_CONTENT));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_TITLE));
            long dateEpoch = cursor.getLong(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DATE));
            int completed = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_COMPLETED));
            int deleted = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DELETED));

            int priorityId = cursor.getInt(cursor.getColumnIndexOrThrow("priority_id"));
            String priorityName = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.PriorityTable.COLUMN_NAME_NAME));
            String priorityColor = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.PriorityTable.COLUMN_NAME_COLOR));

            ToDoPriority toDoPriority = new ToDoPriority(priorityId, priorityName, priorityColor, context);

            toDoItem = new ToDoItem(_id, title, content, dateEpoch, completed == 1, deleted == 1, toDoPriority, context);
            items[counter] = toDoItem;
            counter++;
        }
    } catch (Exception exc) {
        Log.e("Exception - FetchAll", exc.toString());
    } finally {
        cursor.close();
        db.close();
    }

    return items;


    }

    public static ToDoItem[] readAllCompleted(Context context) {
        ToDoItem[] items = null;

        ToDoDbHelper toDoDbHelper = new ToDoDbHelper(context);
        SQLiteDatabase db = toDoDbHelper.getReadableDatabase();

        ToDoItem toDoItem = null;
        Cursor cursor = null;

        try {

            cursor = db.rawQuery("SELECT item._id AS item_id, item.title, item.content, item.date, item.completed, item.deleted, priority.name, priority._id AS priority_id, priority.color FROM item JOIN priority ON item.priority = priority._id WHERE completed = ? AND deleted = ? ORDER BY item_id DESC", new String[]{"1", "0"});
            items = new ToDoItem[cursor.getCount()];
            int counter = 0;

            while (cursor.moveToNext()) {

                int _id = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_CONTENT));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_TITLE));
                long dateEpoch = cursor.getLong(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DATE));
                int completed = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_COMPLETED));
                int deleted = cursor.getInt(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.ItemTable.COLUMN_NAME_DELETED));

                int priorityId = cursor.getInt(cursor.getColumnIndexOrThrow("priority_id"));
                String priorityName = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.PriorityTable.COLUMN_NAME_NAME));
                String priorityColor = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDatabaseContract.PriorityTable.COLUMN_NAME_COLOR));

                ToDoPriority toDoPriority = new ToDoPriority(priorityId, priorityName, priorityColor, context);

                toDoItem = new ToDoItem(_id, title, content, dateEpoch, completed == 1, deleted == 1, toDoPriority, context);
                items[counter] = toDoItem;
                counter++;
            }
        } catch (Exception exc) {
            Log.e("Exception - FetchAll", exc.toString());
        } finally {
            cursor.close();
            db.close();
        }

        return items;


    }

    public String validationMessage;

    public boolean isValid(){

        boolean isValid = true;

        if(title == null || title.equals("")){
            isValid = false;
            validationMessage = "Title is required field.";
        }

        return isValid;
    }

}