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

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TaskDeleteDialog.TaskDeleteListener {
   static int REQUEST_CODE = 101;
   TasksAdapter mAdapter;
   // sort criteria, which is sort by Name by default
   int mCriteria = 0;
   TaskDatabase mDatabase;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // Stetho.initializeWithDefaults(this);
      mDatabase = TaskDatabase.instance(this);
      // List<Task> tasks = generateTasks();
      List<Task> tasks = mDatabase.query();
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
            mDatabase.add(task);
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
      // create an array of String's, for ease of declaration, as opposed to that of a List
      String[] strings = {
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Ein", "Zwei", "Drei", "Vier", "Funf", "Sechs", "Sieben", "Acht", "Neun", "Zehn",
            "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix",
            "Uno", "Dos", "Tres", "Quatro", "Cinco", "Seis", "Siete", "Ocho", "Nueve", "Diez"
      };
      // copy the contents of the array into a List
      List<String> names = new ArrayList<>();
      for (String string : strings) {
         names.add(string);
      }
      // prepare the random generator
      Random random = new Random();
      List<Task> tasks = new ArrayList<>();

      while (names.size() != 0) {
         String uuid = UUID.randomUUID().toString();
         // pick a random name from the List
         int randomName = random.nextInt(names.size());
         // remove the name from the List when done
         String name = names.remove(randomName);
         // pick a random day from 5 days in the past until 5 days in the future
         Calendar calendar = Calendar.getInstance();
         int randomDay = random.nextInt(10) - 5;
         calendar.add(Calendar.DAY_OF_MONTH, randomDay);
         // pick a random hour
         int randomHour = random.nextInt(24);
         calendar.set(Calendar.HOUR_OF_DAY, randomHour);
         // pick a random minute
         int randomMinute = random.nextInt(60);
         calendar.set(Calendar.MINUTE, randomMinute);
         // pick a random priority (0 = High, 1 = Medium, and 2 = Low)
         int randomPriority = random.nextInt(3);
         // create a new Task with the random name, random date, and random priority
         Task task = new Task(uuid, name, calendar.getTime(), randomPriority, "Empty");
         tasks.add(task);
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
