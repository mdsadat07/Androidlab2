package com.example.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {
    private TextView tvGreeting;
    private Button btnDontCall, btnThankYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        // Initialize TextView and Buttons
        tvGreeting = findViewById(R.id.tv_greeting);
        btnDontCall = findViewById(R.id.btn_dont_call_me);
        btnThankYou = findViewById(R.id.btn_thank_you);

        // Retrieve the name passed from the previous activity
        String name = getIntent().getStringExtra("name");

        // Set the greeting text with the name, or use a fallback if name is null
        if (name != null && !name.isEmpty()) {
            tvGreeting.setText("Welcome, " + name + "!");
        } else {
            tvGreeting.setText("Welcome!");
        }

        // Set up the button click listeners
        btnDontCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NameActivity.this, "Don't call me that!", Toast.LENGTH_SHORT).show();
            }
        });

        btnThankYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NameActivity.this, "Thank you!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
