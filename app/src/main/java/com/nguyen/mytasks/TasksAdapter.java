package com.nguyen.mytasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
      int textColor = diff > 0 ? Color.RED : Color.GREEN;
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
            remove(task);
         }
      });

      return convertView;
   }

   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == REQUEST_CODE) {
         if (resultCode == ((Activity)getContext()).RESULT_OK) {
            Task updatedTask = (Task)data.getSerializableExtra("TASK_OUT");
            Log.d("TRUONG", "TaskAdapter::" + updatedTask);
            Task currentTask = getItem(mPosition);
            currentTask.update(updatedTask);
            notifyDataSetChanged();
         }
      }
   }
}
