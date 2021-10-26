package com.link.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.link.todo.core.ToDoItem;
import com.link.todo.core.ToDoItemAdapter;

public class ItemsPreviewFragment extends Fragment {

    ListView listView;
    String mode;

    public ItemsPreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_items_preview, container, false);

        listView = (ListView) view.findViewById(R.id.itemsListView);
        TextView textView = (TextView) view.findViewById(R.id.emptyTextView);
        listView.setEmptyView(textView);

        Bundle args = getArguments();
        mode = args.getString("mode", "active");

        if (mode.equals("active")) {
            loadActiveItems(listView);
        } else {
            loadCompletedItems(listView);
        }

        return view;
    }

    private void loadActiveItems(ListView listView) {


        ToDoItem[] items = ToDoItem.readAllActive(getActivity());
        final ToDoItemAdapter adapter = new ToDoItemAdapter(getActivity(), R.layout.todo_item_layout, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem toDoItem = adapter.getItem(position);

                Intent intent = new Intent(getContext(), ItemPreviewActivity.class);
                intent.putExtra("item", toDoItem);
                startActivity(intent);
            }
        });
    }

    private void loadCompletedItems(ListView listView) {
        ToDoItem[] items = ToDoItem.readAllCompleted(getActivity());
        final ToDoItemAdapter adapter = new ToDoItemAdapter(getActivity(), R.layout.todo_item_layout, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem toDoItem = adapter.getItem(position);

                Intent intent = new Intent(getContext(), ItemPreviewActivity.class);
                intent.putExtra("link.todo.ToDoItem", toDoItem);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mode.equals("active")) {
            loadActiveItems(listView);
        } else {
            loadCompletedItems(listView);
        }

    }
}