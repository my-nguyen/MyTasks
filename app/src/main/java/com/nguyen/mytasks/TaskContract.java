package com.nguyen.mytasks;

import android.provider.BaseColumns;

/**
 * Created by My on 9/6/2016.
 */
public class TaskContract {
   public static final class TaskEntry implements BaseColumns {
      public static final String TABLE_NAME = "task";
      public static final String COLUMN_UUID = "uuid";
      public static final String COLUMN_NAME = "name";
      public static final String COLUMN_DATE = "date";
      public static final String COLUMN_PRIORITY = "priority";
      public static final String COLUMN_NOTE = "note";
   }
}
