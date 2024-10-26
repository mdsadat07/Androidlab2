package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView heightTextView = view.findViewById(R.id.heightTextView);
        TextView massTextView = view.findViewById(R.id.massTextView);
        Button backButton = view.findViewById(R.id.backButton);

        // Get data from the bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            String height = bundle.getString("height");
            String mass = bundle.getString("mass");

            nameTextView.setText(name);
            heightTextView.setText(height);
            massTextView.setText(mass);
        }

        // Set up Back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the previous fragment or activity
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }
}
