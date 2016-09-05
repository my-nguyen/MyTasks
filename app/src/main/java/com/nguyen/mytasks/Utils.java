package com.nguyen.mytasks;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by My on 9/4/2016.
 */
public class Utils {
   public static String timeAgo(long timeCreated) {
      // get the "20 minutes ago", "2 hours ago" string
      CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
            timeCreated * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
      // split string into tokens
      String[] tokens = relativeTime.toString().split(" ");
      // return number appended with "m" or "h", e.g. 20m, 2h
      return tokens[0] + tokens[1].charAt(0);
   }

   public static String getDateFromCalendar(Calendar calendar) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
      return dateFormat.format(calendar.getTime());
   }

   public static String getTimeFromCalendar(Calendar calendar) {
      SimpleDateFormat timeFormat = new SimpleDateFormat("h:m a");
      return timeFormat.format(calendar.getTime());
   }
}
