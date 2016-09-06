package com.nguyen.mytasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 9/6/2016.
 */
public class TaskDatabase {
   static TaskDatabase sInstance;
   Context mContext;
   SQLiteDatabase mDatabase;

   public static TaskDatabase instance(Context context) {
      if (sInstance == null) {
         sInstance = new TaskDatabase(context);
      }
      return sInstance;
   }

   private TaskDatabase(Context context) {
      // mContext = context.getApplicationContext();
      mContext = context;
      TaskDbHelper dbHelper = new TaskDbHelper(context);
      mDatabase = dbHelper.getWritableDatabase();
   }

   public List<Task> query() {
      List<Task> tasks = new ArrayList<>();
      Cursor cursor = mDatabase.query(TaskContract.TaskEntry.TABLE_NAME, null, null, null, null, null, null);
      try {
         cursor.moveToFirst();
         while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
            long date = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DATE));
            int priority = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY));
            String note = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NOTE));
            Task task = new Task(name, date, priority, note);
            tasks.add(task);
            cursor.moveToNext();
         }
      } finally {
         cursor.close();
      }

      return tasks;
   }

   public void add(Task task) {
      ContentValues values = getContentValues(task);
      mDatabase.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
   }

   private ContentValues getContentValues(Task task) {
      ContentValues values = new ContentValues();
      values.put(TaskContract.TaskEntry.COLUMN_NAME, task.name);
      values.put(TaskContract.TaskEntry.COLUMN_DATE, task.date.getTime());
      values.put(TaskContract.TaskEntry.COLUMN_PRIORITY, task.priority);
      values.put(TaskContract.TaskEntry.COLUMN_NOTE, task.note);
      return values;
   }
}
