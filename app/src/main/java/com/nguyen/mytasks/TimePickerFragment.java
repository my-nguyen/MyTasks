package com.nguyen.mytasks;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by My on 9/4/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
   interface TimePickerListener {
      void onFinishTime(int hour, int minute);
   }

   public static TimePickerFragment newInstance(Calendar calendar)
   {
      Bundle bundle = new Bundle();
      bundle.putSerializable("CALENDAR_IN", calendar);
      TimePickerFragment fragment = new TimePickerFragment();
      fragment.setArguments(bundle);
      return fragment;
   }

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      Calendar calendar = (Calendar)getArguments().getSerializable("CALENDAR_IN");
      int hour = calendar.get(Calendar.HOUR_OF_DAY);
      int minute = calendar.get(Calendar.MINUTE);
      return new TimePickerDialog(getActivity(), this, hour, minute, false);
   }

   @Override
   public void onTimeSet(TimePicker timePicker, int i, int i1) {
      TimePickerListener listener = (TimePickerListener)getActivity();
      listener.onFinishTime(i, i1);
   }
}
