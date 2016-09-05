package com.nguyen.mytasks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by My on 9/4/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
   interface DatePickerListener {
      void onFinishDate(int year, int month, int day);
   }

   public static DatePickerFragment newInstance(Calendar calendar)
   {
      Bundle bundle = new Bundle();
      bundle.putSerializable("CALENDAR_IN", calendar);
      DatePickerFragment fragment = new DatePickerFragment();
      fragment.setArguments(bundle);
      return fragment;
   }

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      Calendar calendar = (Calendar)getArguments().getSerializable("CALENDAR_IN");
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
