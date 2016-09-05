package com.nguyen.mytasks;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   static int REQUEST_CODE = 100;
   TasksAdapter mAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      List<Task> tasks = generateTasks();
      mAdapter = new TasksAdapter(this, tasks);
      ListView list = (ListView)findViewById(R.id.tasks);
      list.setAdapter(mAdapter);

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
         if (resultCode == RESULT_OK) {
            mAdapter.onActivityResult(requestCode, resultCode, data);
         }
      }
   }

   private List<Task> generateTasks() {
      List<String> names = Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten");
      List<Task> tasks = new ArrayList<>();
      Calendar calendar = Calendar.getInstance();
      int add = -(names.size() / 2);
      calendar.add(Calendar.DAY_OF_MONTH, add);
      for (String name : names) {
         Task task = new Task(name, calendar.getTime(), 0, "Empty note");
         Log.d("TRUONG", "generateTasks::" + task);
         tasks.add(task);
         calendar.add(Calendar.DAY_OF_MONTH, 1);
      }
      return tasks;
   }
}
