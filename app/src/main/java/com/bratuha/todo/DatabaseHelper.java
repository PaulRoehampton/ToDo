package com.bratuha.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_COMPLETED = "completed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_COMPLETED + " INTEGER"
                + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1);
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }
}
