package com.nguyen.mytasks;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by My on 9/4/2016.
 */
public class Task implements Serializable {
   public String name;
   public Calendar calendar;
   public int priority;
   public String note;

   public Task() {
      name = "";
      calendar = Calendar.getInstance();
      priority = 0;
      note = "";
   }

   public Task(String name, Calendar calendar, int priority, String note) {
      this.name = name;
      this.calendar = calendar;
      this.priority = priority;
      this.note = note;
   }

   public void update(Task rhs) {
      name = rhs.name;
      calendar = rhs.calendar;
      priority = rhs.priority;
      note = rhs.note;
   }

   @Override
   public String toString() {
      final String SEPARATOR = "::";
      StringBuilder builder = new StringBuilder();
      builder.append(name).append(SEPARATOR);
      Date date = calendar.getTime();
      SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy h:m a");
      String dateString = format.format(date);
      builder.append(dateString).append(SEPARATOR);
      builder.append(priority).append(SEPARATOR);
      builder.append(note);
      return builder.toString();
   }
}
