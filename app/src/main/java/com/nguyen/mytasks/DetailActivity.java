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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity
      implements DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener {
   Task task;
   TextView dueDate;
   TextView dueTime;

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
      dueDate = (TextView)findViewById(R.id.due_date);
      ImageButton datePicker = (ImageButton)findViewById(R.id.date_picker);
      dueTime = (TextView)findViewById(R.id.due_time);
      ImageButton timePicker = (ImageButton)findViewById(R.id.time_picker);
      Spinner prioritySpinner = (Spinner)findViewById(R.id.priority_spinner);
      final EditText noteText = (EditText)findViewById(R.id.note_text);
      Button save = (Button)findViewById(R.id.save);

      task = (Task)getIntent().getSerializableExtra("TASK_IN");
      if (task == null) {
         task = new Task();
      } else {
         taskName.setText(task.name);

         final Calendar calendar = task.calendar;
         dueDate.setText(getDateFromCalendar(calendar));
         dueTime.setText(getTimeFromCalendar(calendar));

         datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DialogFragment fragment = DatePickerFragment.newInstance(calendar);
               fragment.show(getSupportFragmentManager(), "DATE_PICKER");
            }
         });
         timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DialogFragment fragment = TimePickerFragment.newInstance(calendar);
               fragment.show(getSupportFragmentManager(), "TIME_PICKER");
            }
         });

         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.priority_array, android.R.layout.simple_spinner_item);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         prioritySpinner.setAdapter(adapter);
         prioritySpinner.setSelection(task.priority);
         prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               task.priority = position;
               Log.d("TRUONG", "priority: " + task.priority);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
         });

         noteText.setText(task.note);
      }

      save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            task.name = taskName.getText().toString();
            task.note = noteText.getText().toString();
            Log.d("TRUONG", "TASK::" + task);
            Intent i = new Intent();
            i.putExtra("TASK_OUT", task);
            setResult(RESULT_OK, i);
            finish();
         }
      });
   }

   @Override
   public void onFinishDate(int year, int month, int day) {
      task.calendar.set(Calendar.YEAR, year);
      task.calendar.set(Calendar.MONTH, month);
      task.calendar.set(Calendar.DAY_OF_MONTH, day);
      dueDate.setText(getDateFromCalendar(task.calendar));
   }

   @Override
   public void onFinishTime(int hour, int minute) {
      task.calendar.set(Calendar.HOUR_OF_DAY, hour);
      task.calendar.set(Calendar.MINUTE, minute);
      dueTime.setText(getTimeFromCalendar(task.calendar));
   }

   private String getDateFromCalendar(Calendar calendar) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
      return dateFormat.format(calendar.getTime());
   }

   private String getTimeFromCalendar(Calendar calendar) {
      SimpleDateFormat timeFormat = new SimpleDateFormat("h:m a");
      return timeFormat.format(calendar.getTime());
   }
}
