package com.example.androidlabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> todos;
    private TodoDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView todoListView = findViewById(R.id.todoListView);
        EditText todoInput = findViewById(R.id.todoInput);
        Switch urgencySwitch = findViewById(R.id.urgencySwitch);
        Button addButton = findViewById(R.id.addButton);

        dbHelper = new TodoDatabaseHelper(this);
        todos = new ArrayList<>();

        // Load todos from the database
        loadTodosFromDB();

        // Custom Adapter for ListView
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_todo, R.id.todoText, todos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.todoText);

                // Set red background for urgent tasks
                if (todos.get(position).contains("Urgent: ")) {
                    view.setBackgroundColor(Color.RED);
                    textView.setTextColor(Color.WHITE); // Make text visible on red background
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        todoListView.setAdapter(adapter);

        // Add button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = todoInput.getText().toString();
                boolean isUrgent = urgencySwitch.isChecked();

                if (!todoText.isEmpty()) {
                    // Add to the database
                    dbHelper.addTodo(todoText, isUrgent ? 1 : 0);

                    // Add to list view
                    String displayText = isUrgent ? "Urgent: " + todoText : todoText;
                    todos.add(displayText);
                    adapter.notifyDataSetChanged();

                    // Clear input field
                    todoInput.setText("");
                }
            }
        });

        // Long click handler to delete an item
        todoListView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Show confirmation dialog
            showDeleteConfirmationDialog(position);
            return true;
        });
    }

    // Load todos from the database
    private void loadTodosFromDB() {
        Cursor cursor = dbHelper.getAllTodos();

        while (cursor.moveToNext()) {
            String todo = cursor.getString(cursor.getColumnIndex("todo"));
            int urgency = cursor.getInt(cursor.getColumnIndex("urgency"));
            String displayText = (urgency == 1 ? "Urgent: " : "") + todo;
            todos.add(displayText);
        }
        cursor.close();
    }

    // Show a confirmation dialog before deleting an item
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to delete this?");
        builder.setMessage("Selected row is: " + (position + 1));

        // Set up the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the selected item
                dbHelper.deleteTodoById(position + 1); // Adjust for 0-based index
                todos.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Close the dialog if the user cancels
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
