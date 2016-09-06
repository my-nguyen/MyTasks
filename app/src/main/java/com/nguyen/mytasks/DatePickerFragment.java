package com.nguyen.mytasks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by My on 9/4/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
   interface DatePickerListener {
      void onFinishDate(int year, int month, int day);
   }

   public static DatePickerFragment newInstance(Date date)
   {
      Bundle bundle = new Bundle();
      bundle.putSerializable("DATE_IN", date);
      DatePickerFragment fragment = new DatePickerFragment();
      fragment.setArguments(bundle);
      return fragment;
   }

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      // receive a Date object
      Date date = (Date)getArguments().getSerializable("DATE_IN");
      // convert the Date object to a Calendar object, to extract the year, month and day, for
      // displaying them in the DatePickerDialog
      Calendar calendar = Calendar.getInstance();
      if (date != null) {
         calendar.setTime(date);
      }
      Log.d("TRUONG", "DatePickerFragment::date: " + date);
      int year = calendar.get(Calendar.YEAR);
      int monthOfYear = calendar.get(Calendar.MONTH);
      int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
      return new DatePickerDialog(getActivity(), this, year, monthOfYear, dayOfMonth);
   }

   @Override
   public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
      DatePickerListener listener = (DatePickerListener)getActivity();
      listener.onFinishDate(i, i1, i2);
   }
}
