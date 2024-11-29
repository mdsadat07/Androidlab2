package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TODOS = "todos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TODO = "todo";
    public static final String COLUMN_URGENCY = "urgency";

    private static final String TABLE_CREATE =
        "CREATE TABLE " + TABLE_TODOS + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_TODO + " TEXT, " +
        COLUMN_URGENCY + " INTEGER);";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }

    /**
     * Add a new to-do item to the database.
     * 
     * @param todo The to-do text
     * @param urgency The urgency of the to-do (1 for urgent, 0 for not urgent)
     */
    public void addTodo(String todo, int urgency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO, todo);
        values.put(COLUMN_URGENCY, urgency);
        db.insert(TABLE_TODOS, null, values);
    }

    /**
     * Add a new to-do item to the database and return its ID.
     * 
     * @param todo The to-do text
     * @param urgency The urgency of the to-do (1 for urgent, 0 for not urgent)
     * @return The ID of the newly added to-do item
     */
    public int addTodoWithReturnId(String todo, int urgency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO, todo);
        values.put(COLUMN_URGENCY, urgency);
        long newRowId = db.insert(TABLE_TODOS, null, values);
        return (int) newRowId;
    }

    /**
     * Retrieve all to-do items from the database.
     * 
     * @return Cursor pointing to the result set
     */
    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TODOS, null, null, null, null, null, null);
    }

    /**
     * Delete a to-do item from the database by its ID.
     * 
     * @param id The ID of the to-do item to delete
     */
    public void deleteTodoById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    /**
     * Print the cursor data to the log for debugging.
     * 
     * @param cursor The cursor to print
     */
    public void printCursor(Cursor cursor) {
        Log.d("DatabaseInfo", "DB Version: " + this.getReadableDatabase().getVersion());
        Log.d("DatabaseInfo", "Number of Columns: " + cursor.getColumnCount());
        Log.d("DatabaseInfo", "Column Names: " + String.join(", ", cursor.getColumnNames()));
        Log.d("DatabaseInfo", "Number of Results: " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                Log.d("DatabaseRow", "ID: " + cursor.getLong(0) +
                        ", Todo: " + cursor.getString(1) +
                        ", Urgency: " + cursor.getInt(2));
            } while (cursor.moveToNext());
        }
    }
}