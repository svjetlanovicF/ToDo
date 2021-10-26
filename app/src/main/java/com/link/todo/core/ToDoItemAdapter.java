package com.link.todo.core;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.link.todo.R;

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

    private Context context;
    private int layoutResourceId;
    private ToDoItem data[] = null;

    public ToDoItemAdapter(Context context, int layoutResourceId, ToDoItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        ToDoItemHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ToDoItemHolder();
            holder.colorView = row.findViewById(R.id.rectangle_at_the_top);
            holder.titleTextView = (TextView) row.findViewById(R.id.titleTextView);
            holder.contentTextView = (TextView) row.findViewById(R.id.dateTextView);

            holder.dayTextView = (TextView)row.findViewById(R.id.dayTextView);
            holder.monthTextView = (TextView)row.findViewById(R.id.monthTextView);
            holder.yearTextView = (TextView)row.findViewById(R.id.yearTextView);

            row.setTag(holder);
        } else {
            holder = (ToDoItemHolder) row.getTag();
        }

        ToDoItem todoItem = data[position];

        if(!todoItem.getCompleted()){
            holder.colorView.setBackgroundColor(todoItem.getToDoPriority().getUiColor()[0]);
        } else {
            holder.colorView.setBackgroundColor(ContextCompat.getColor(context, R.color.completed));
        }

        holder.titleTextView.setText(todoItem.getTitle());
        holder.contentTextView.setText(todoItem.getContent());

        holder.dayTextView.setText(todoItem.getDay());
        holder.monthTextView.setText(todoItem.getMonth());
        holder.yearTextView.setText(todoItem.getYear());

        return row;

    }


    private static class ToDoItemHolder {

        View colorView;

        TextView titleTextView;
        TextView contentTextView;

        TextView dayTextView;
        TextView monthTextView;
        TextView yearTextView;
    }
}
