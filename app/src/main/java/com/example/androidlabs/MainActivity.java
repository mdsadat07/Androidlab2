package com.example.androidlabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<TodoItem> todoList = new ArrayList<>();
    TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        EditText editText = findViewById(R.id.editText);
        Switch urgentSwitch = findViewById(R.id.urgentSwitch);
        Button addButton = findViewById(R.id.addButton);

        adapter = new TodoAdapter();
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editText.getText().toString();
                boolean isUrgent = urgentSwitch.isChecked();
                todoList.add(new TodoItem(task, isUrgent));
                editText.setText(""); // Clear the EditText
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        todoList.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    class TodoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int position) {
            return todoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
            }

            TodoItem currentItem = todoList.get(position);
            TextView todoText = convertView.findViewById(R.id.todoText);
            todoText.setText(currentItem.getText());

            if (currentItem.isUrgent()) {
                convertView.setBackgroundColor(Color.RED);
                todoText.setTextColor(Color.WHITE);
            } else {
                convertView.setBackgroundColor(Color.WHITE);
                todoText.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }
}
