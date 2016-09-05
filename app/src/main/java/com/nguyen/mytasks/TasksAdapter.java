package com.nguyen.mytasks;

import android.content.Context;
import android.content.Intent;
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
   static class ViewHolder {
      TextView name;
      TextView date;
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
         viewHolder.edit = (ImageButton) convertView.findViewById(R.id.edit);
         viewHolder.delete = (ImageButton)convertView.findViewById(R.id.delete);
         convertView.setTag(viewHolder);
      } else {
         viewHolder = (ViewHolder)convertView.getTag();
      }

      final Task task = getItem(position);
      viewHolder.name.setText(task.name);
      // viewHolder.date.setText(task.date);
      viewHolder.edit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent i = DetailActivity.newIntent(getContext(), task);
            getContext().startActivity(i);
         }
      });

      return convertView;
   }
}
