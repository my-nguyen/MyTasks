package com.nguyen.mytasks;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskDeleteDialog.TaskDeleteListener {
   static int REQUEST_CODE = 101;
   TasksAdapter mAdapter;
   int mCriteria = 0;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      List<Task> tasks = generateTasks();
      mAdapter = new TasksAdapter(this, tasks);
      ListView list = (ListView)findViewById(R.id.tasks);
      list.setAdapter(mAdapter);

      Spinner sortOrder = (Spinner)findViewById(R.id.sort_spinner);
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.sort_order_array, android.R.layout.simple_spinner_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      sortOrder.setAdapter(adapter);
      sortOrder.setSelection(adapter.getPosition("By Date"));
      sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mCriteria = position;
            sortByCriteria();
         }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
      });

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent i = DetailActivity.newIntent(MainActivity.this, null);
            startActivityForResult(i, REQUEST_CODE);
         }
      });
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == REQUEST_CODE) {
         // request code matches: this is data resulting from a click on FloatingActionButton,
         // which is to add a new Task
         if (resultCode == RESULT_OK) {
            Task task = (Task)data.getSerializableExtra("TASK_OUT");
            mAdapter.add(task);
            sortByCriteria();
         }
      } else {
         // request code doesn't match: this is data from TasksAdapter (which is to edit an
         // existing Task): forward this action to TasksAdapter
         mAdapter.onActivityResult(requestCode, resultCode, data);
         sortByCriteria();
      }
   }

   @Override
   public void onTaskDeleteOK() {
      mAdapter.onDeleteOK();
   }

   private void sortByCriteria() {
      switch (mCriteria) {
         case 0:
            mAdapter.sort(new DateComparator());
            break;
         case 1:
            mAdapter.sort(new NameComparator());
            break;
         case 2:
            mAdapter.sort(new PriorityComparator());
            break;
      }
      mAdapter.notifyDataSetChanged();
   }

   private List<Task> generateTasks() {
      List<String> names = Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten");
      List<Task> tasks = new ArrayList<>();
      Calendar calendar = Calendar.getInstance();
      int add = -(names.size() / 2);
      calendar.add(Calendar.DAY_OF_MONTH, add);
      for (String name : names) {
         Task task = new Task(name, calendar.getTime(), 0, "Empty note");
         tasks.add(task);
         calendar.add(Calendar.DAY_OF_MONTH, 1);
      }
      return tasks;
   }

   class DateComparator implements Comparator<Task> {
      @Override
      public int compare(Task task1, Task task2) {
         return (int)(task1.date.getTime() - task2.date.getTime());
      }
   }

   class NameComparator implements Comparator<Task> {
      @Override
      public int compare(Task task1, Task task2) {
         return task1.name.compareTo(task2.name);
      }
   }

   class PriorityComparator implements Comparator<Task> {
      @Override
      public int compare(Task task1, Task task2) {
         return task1.priority - task2.priority;
      }
   }
}
