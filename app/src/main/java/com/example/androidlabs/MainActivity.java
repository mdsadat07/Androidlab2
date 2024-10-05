package com.example.androidlabs;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button pressMeButton;
    private EditText editText;
    private CheckBox checkBox;
    private Switch switch1;
    private ImageButton flagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint);  // Set the layout to Constraint Layout

        textView = findViewById(R.id.textView);
        pressMeButton = findViewById(R.id.pressMeButton);
        editText = findViewById(R.id.editText);
        checkBox = findViewById(R.id.checkBox);
        switch1 = findViewById(R.id.switch1);
        flagButton = findViewById(R.id.flagButton);

        // Button onClickListener
        pressMeButton.setOnClickListener(v -> {
            String enteredText = editText.getText().toString();
            textView.setText(enteredText);

            // Show Toast message
            Toast.makeText(MainActivity.this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
        });

        // CheckBox onCheckedChangeListener
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String state = isChecked ? getString(R.string.on) : getString(R.string.off);
            String snackMessage = getString(R.string.checkbox_state, state);
            Snackbar snackbar = Snackbar.make(buttonView, snackMessage, Snackbar.LENGTH_LONG);

            // Undo action
            snackbar.setAction(getString(R.string.undo), v -> checkBox.setChecked(!isChecked));
            snackbar.show();
        });
    }
}