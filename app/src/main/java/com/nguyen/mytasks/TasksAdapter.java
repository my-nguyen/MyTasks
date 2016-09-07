package com.nguyen.mytasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by My on 9/4/2016.
 */
public class TasksAdapter extends ArrayAdapter<Task> {
   static final int REQUEST_CODE = 100;
   int mPosition;
   Task mTaskToDelete;

   static class ViewHolder {
      TextView name;
      TextView date;
      TextView priority;
      ImageButton edit;
      ImageButton delete;
   }

   public TasksAdapter(Context context, List<Task> tasks) {
      super(context, 0, tasks);
   }

   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
         LayoutInflater inflater = LayoutInflater.from(getContext());
         convertView = inflater.inflate(R.layout.item_task, parent, false);
         viewHolder = new ViewHolder();
         viewHolder.name = (TextView)convertView.findViewById(R.id.name);
         viewHolder.date = (TextView)convertView.findViewById(R.id.date);
         viewHolder.priority = (TextView)convertView.findViewById(R.id.priority);
         viewHolder.edit = (ImageButton) convertView.findViewById(R.id.edit);
         viewHolder.delete = (ImageButton)convertView.findViewById(R.id.delete);
         convertView.setTag(viewHolder);
      } else {
         viewHolder = (ViewHolder)convertView.getTag();
      }

      final Task task = getItem(position);
      viewHolder.name.setText(task.name);

      long diff = System.currentTimeMillis() - task.date.getTime();
      int textColor;
      if (diff > 0) {
         // task has expired
         textColor = Color.RED;
      } else if (diff >= -86400000) {
         // task scheduled within one day
         textColor = Color.parseColor("#FFD700");
      } else {
         // task beyond one day
         // textColor = Color.GREEN;
         textColor = Color.parseColor("#006400");
      }
      viewHolder.date.setTextColor(textColor);
      String date = Utils.getShortDateFromDate(task.date);
      String time = Utils.getTimeFromDate(task.date);
      viewHolder.date.setText(date + " at " + time);

      String[] priorities = { "High", "Medium", "Low" };
      viewHolder.priority.setTextColor(textColor);
      viewHolder.priority.setText(priorities[task.priority]);

      viewHolder.edit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // save the current position, so onActivityResult() can update properly
            mPosition = position;
            Intent i = DetailActivity.newIntent(getContext(), task);
            ((Activity)getContext()).startActivityForResult(i, REQUEST_CODE);
         }
      });

      viewHolder.delete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // mark this task for delete
            mTaskToDelete = task;
            // TaskDeleteDialog, which is a DialogFragment, requires
            // android.support.v4.app.FragmentManager and not android.app.FragmentManager
            // the former is obtained via getSupportFragmentManager() which is from MainActivity
            // the latter is obtained via getFragmentManager() which is from Activity
            TaskDeleteDialog dialog = new TaskDeleteDialog();
            FragmentManager manager = ((MainActivity)getContext()).getSupportFragmentManager();
            dialog.show(manager, "TASK_DELETE_DIALOG");
         }
      });

      return convertView;
   }

   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == REQUEST_CODE) {
         if (resultCode == ((Activity)getContext()).RESULT_OK) {
            Task updatedTask = (Task)data.getSerializableExtra("TASK_OUT");
            Task currentTask = getItem(mPosition);
            currentTask.update(updatedTask);
         }
      }
   }

   // on the Task Delete dialog, when user presses OK, since the dialog is a fragment, it could
   // only communicate back to the Activity invoking it, which is MainActivity, and not
   // TasksAdapter. so MainActivity must implement the communication callback, which calls this
   // method in order to do the actual removal of the Task record.
   public void onDeleteOK() {
      remove(mTaskToDelete);
   }
}
