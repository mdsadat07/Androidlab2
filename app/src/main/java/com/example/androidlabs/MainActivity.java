package com.example.androidlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button prevButton, nextButton;
    ArrayList<String> characterNames = new ArrayList<>();
    ArrayList<JSONObject> characterDetails = new ArrayList<>();
    BaseAdapter adapter;

    // Track the current page of the API
    int currentPage = 1;
    int totalPages = 9;  // Total pages available on swapi.dev for people

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        adapter = new CharacterAdapter(this, characterNames);
        listView.setAdapter(adapter);

        // Set up ListView onItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View frameLayout = findViewById(R.id.frameLayout);
                JSONObject character = characterDetails.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("name", character.optString("name"));
                bundle.putString("height", character.optString("height"));
                bundle.putString("mass", character.optString("mass"));

                if (frameLayout == null) {
                    // On a phone, launch EmptyActivity
                    Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // On a tablet, replace fragment
                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.setArguments(bundle);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, detailsFragment)
                            .commit();
                }
            }
        });

        // Fetch the first page of data
        fetchData(currentPage);

        // Set up Next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < totalPages) {
                    currentPage++;
                    fetchData(currentPage);
                }
            }
        });

        // Set up Previous Button
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    fetchData(currentPage);
                }
            }
        });
    }

    // Fetch data for the specified page
    private void fetchData(int page) {
        String url = "https://swapi.dev/api/people/?page=" + page + "&format=json";
        new FetchCharactersTask().execute(url);

        // Update button states
        prevButton.setEnabled(page > 1);
        nextButton.setEnabled(page < totalPages);
    }

    // AsyncTask to fetch Star Wars characters
    class FetchCharactersTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                Log.d("FetchCharactersTask", "API Response: " + result.toString());
            } catch (Exception e) {
                Log.e("FetchCharactersTask", "Error fetching data", e);
                return null;
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String responseText) {
            if (responseText == null) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray characters = new JSONObject(responseText).getJSONArray("results");
                characterNames.clear();
                characterDetails.clear();

                for (int i = 0; i < characters.length(); i++) {
                    JSONObject character = characters.getJSONObject(i);
                    characterNames.add(character.getString("name"));
                    characterDetails.add(character);
                }
                adapter.notifyDataSetChanged();
                Log.d("FetchCharactersTask", "Characters loaded: " + characterNames);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                Log.e("FetchCharactersTask", "Error parsing data", e);
            }
        }
    }
}
