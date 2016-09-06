package com.nguyen.mytasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity
      implements DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener {
   Task mTask;
   TextView mDueDate;
   TextView mDueTime;

   public static Intent newIntent(Context context, Task task) {
      Intent i = new Intent(context, DetailActivity.class);
      i.putExtra("TASK_IN", task);
      return i;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_detail);

      final EditText taskName = (EditText)findViewById(R.id.task_name);
      mDueDate = (TextView)findViewById(R.id.due_date);
      ImageButton datePicker = (ImageButton)findViewById(R.id.date_picker);
      mDueTime = (TextView)findViewById(R.id.due_time);
      ImageButton timePicker = (ImageButton)findViewById(R.id.time_picker);
      Spinner prioritySpinner = (Spinner)findViewById(R.id.priority_spinner);
      final EditText noteText = (EditText)findViewById(R.id.note_text);
      Button save = (Button)findViewById(R.id.save);
      Button cancel = (Button)findViewById(R.id.cancel);

      mTask = (Task)getIntent().getSerializableExtra("TASK_IN");
      if (mTask == null) {
         mTask = new Task();
         getSupportActionBar().setTitle("New Task");
      } else {
         getSupportActionBar().setTitle("Edit Task");
      }

      taskName.setText(mTask.name);

      final Date date = mTask.date;
      mDueDate.setText(Utils.getLongDateFromDate(date));
      mDueTime.setText(Utils.getTimeFromDate(date));

      datePicker.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            DialogFragment fragment = DatePickerFragment.newInstance(date);
            fragment.show(getSupportFragmentManager(), "DATE_PICKER");
         }
      });
      timePicker.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            DialogFragment fragment = TimePickerFragment.newInstance(date);
            fragment.show(getSupportFragmentManager(), "TIME_PICKER");
         }
      });

      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.priority_array, android.R.layout.simple_spinner_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      prioritySpinner.setAdapter(adapter);
      prioritySpinner.setSelection(mTask.priority);
      prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mTask.priority = position;
         }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
      });

      noteText.setText(mTask.note);

      save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // save data; the date, time and priority have all been saved already
            mTask.name = taskName.getText().toString();
            mTask.note = noteText.getText().toString();

            // send the data back to the calling Activity
            Intent i = new Intent();
            i.putExtra("TASK_OUT", mTask);
            setResult(RESULT_OK, i);

            // dismiss this Activity
            finish();
         }
      });

      cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            finish();
         }
      });
   }

   @Override
   public void onFinishDate(int year, int month, int day) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(mTask.date);
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DAY_OF_MONTH, day);
      mTask.date = calendar.getTime();
      mDueDate.setText(Utils.getLongDateFromDate(mTask.date));
   }

   @Override
   public void onFinishTime(int hour, int minute) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(mTask.date);
      calendar.set(Calendar.HOUR_OF_DAY, hour);
      calendar.set(Calendar.MINUTE, minute);
      mTask.date = calendar.getTime();
      mDueTime.setText(Utils.getTimeFromDate(mTask.date));
   }
}
