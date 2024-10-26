package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private final String[] apiUrls = {
        "https://cataas.com/cat",  // Direct cat image
        "https://cataas.com/cat/says/Hello",  // Cat with "Hello"
        "https://cataas.com/cat/gif"  // Cat GIF
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        // Start the AsyncTask to download and display cat images
        new CatImages().execute();
    }

    // AsyncTask for downloading cat images
    private class CatImages extends AsyncTask<Void, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap catBitmap = null;
            try {
                // Randomly select one of the three API URLs
                String apiUrl = apiUrls[(int) (Math.random() * apiUrls.length)];

                // Fetch the image from the selected API URL
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                catBitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return catBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(ProgressBar.GONE);
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
