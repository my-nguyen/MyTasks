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
   TasksAdapter adapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      List<Task> tasks = generateTasks();
      adapter = new TasksAdapter(this, tasks);
      ListView list = (ListView)findViewById(R.id.tasks);
      list.setAdapter(adapter);

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
            adapter.onActivityResult(requestCode, resultCode, data);
            // adapter.notifyDataSetChanged();
         }
      }
   }

   private List<Task> generateTasks() {
      List<String> names = Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten");
      List<Task> tasks = new ArrayList<>();
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_MONTH, -names.size()/2);
      for (String name : names) {
         tasks.add(new Task(name, calendar, 0, name));
         calendar.add(Calendar.DAY_OF_MONTH, 1);
      }
      return tasks;
   }
}
