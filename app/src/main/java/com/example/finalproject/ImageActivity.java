package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * ImageActivity corresponds to activity_image.xml
 */
public class ImageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaring global string variables to store data about the image.
    // These variables can be accessed from anywhere in the class.
    String url = null;
    String longitude = null;
    String latitude = null;
    String imageName = null;

    /**
     * onCreate method called when ImageActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Showing a Toast to notify the user that we're getting the image.
        Toast.makeText(this, getResources().getString(R.string.notify), Toast.LENGTH_SHORT).show();

        // Calling the method setListeners to listen for clicks / taps.
        setListeners();

        // Setting the progress bar to visible.
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Setting both these TextViews to invisible.
        TextView longLatText = (TextView) findViewById(R.id.lon_lat_text);
        longLatText.setVisibility(View.INVISIBLE);

        TextView textView = (TextView) findViewById(R.id.error_text_view);
        textView.setVisibility(View.INVISIBLE);

        // Setting this button to invisible.
        Button button = (Button) findViewById(R.id.button_back);
        button.setVisibility(View.INVISIBLE);

        // Getting the intent from the previous activity and extracting the data passed.
        if(getIntent().getExtras() != null) {
            Intent intent = getIntent();
            url = intent.getExtras().getString("url");
            longitude = intent.getExtras().getString("longitude");
            latitude = intent.getExtras().getString("latitude");

            // Logging the data.
            Log.i("url", url);
            Log.i("longitude", longitude);
            Log.i("latitude", latitude);

            // Creating a name for the longitude and latitude that doesn't include dots, but instead dashes.
            // This will be useful later when we use these values for the name of the file since file names can't have dots but can use dashes.
            String longitudeInName = longitude.replaceAll("\\.", "-");
            String latitudeInName = latitude.replaceAll("\\.", "-");

            // Creating an object of the EarthImageQuery and calling it's method execute while passing some data.
            EarthImageQuery earthImageQuery = new EarthImageQuery();
            earthImageQuery.execute(url, longitudeInName, latitudeInName);
        }

        // Creating a toolbar object and adding setting it as a action bar.
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        // Creating a DrawerLayout object and an ActionBarDrawerToggle object with the
        // DrawerLayout object. In addition to this we are adding a drawer listener
        // and syncing the state of the ActionBarDrawerToggle object.
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mTopToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Creating a NavigationView object and setting the navigation item selected
        // listener to the current state.
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Getting the current version and activity name.
        String versionName = BuildConfig.VERSION_NAME;
        String activityName = this.getClass().getSimpleName();

        // Setting the actionBar title.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(activityName + " - " + versionName);
    }

    /**
     * setListeners method listens for button clicks.
     */
    private void setListeners() {
        findViewById(R.id.button_back)
                .setOnClickListener(v ->
                        // this returns us to the previous Activity
                        this.finish()
                );

        findViewById(R.id.favourite_button)
                .setOnClickListener(v -> {
                            // When the "favourite_button" button is clicked we pass various data in an intent and change to the ListActivity class.
                            Intent i = new Intent(ImageActivity.this, ListActivity.class);
                            i.putExtra("date", "2014-02-01");
                            i.putExtra("longitude", longitude);
                            i.putExtra("latitude", latitude);
                            i.putExtra("image_name", imageName);
                            startActivity(i);
                        }
                );

        findViewById(R.id.see_favourites)
                .setOnClickListener(v ->
                        startActivity(
                                new Intent(ImageActivity.this, ListActivity.class)
                        )
                );
    }

    /**
     * onCreateOptionsMenu method that inflates the toolbar_menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * onOptionsItemSelected method that listens for the help_item to be tapped.
     * When it is tapped we display an AlertDialog.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.help_item) {
            new AlertDialog.Builder(this)
                    .setTitle("How to use this page:")
                    .setMessage("If you accessed this page from the navigation drawer, not much will happen. " +
                            "Try accessing this page from the EnterDataActivity page. When you do, an image will appear " +
                            "of the earth at the coordinates you entered. There may however be an error in which case a error " +
                            "message at the bottom of the screen will appear.\n\n" +
                            "Click the 'add to favourites' button to make this image one of your favourites. Alternatively, click " +
                            "the 'see favourites' button to see a list of your favourite images!")
                    .show();
        }

        return true;
    }

    /**
     * onNavigationItemSelected method that listens for the navigation drawer items
     * to be selected.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_activity_option) {

            startActivity(new Intent(this, MainActivity.class));

        } else if (id == R.id.enter_data_activity_option) {

            startActivity(new Intent(this, EnterDataActivity.class));

        } else if (id == R.id.image_activity_option) {

            startActivity(new Intent(this, ImageActivity.class));

        } else if (id == R.id.list_activity_option) {

            startActivity(new Intent(this, ListActivity.class));

        } else if (id == R.id.view_list_activity_option) {

            startActivity(new Intent(this, ViewListActivity.class));

        }


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    /**
     * EarthImageQuery Class that extends AsyncTask and hence is able to run the program asynchronously.
     */
    private class EarthImageQuery extends AsyncTask<String, Integer, String> {

        // Declaring global variables that can be accessed from anywhere in the class.
        private Bitmap image = null;
        boolean failedDownload = false;

        /**
         * doInBackground method that takes whatever was passed to
         * EarthImageQuery when the object of said class was called with
         * the method execute()
         */
        public String doInBackground(String ... args) {
            try {
                // Progressing the progress bar to 25%
                publishProgress(25);

                // Getting the url from the first argument passed
                String realUrl = args[0];

                // The name of an image is its longitude position followed by its latitude.
                // The dots in the longitude and latitude positions have been replaced by dashes.
                // To separate both values there are two dashes.
                // This is done because dots can not be used for file names (but dashes can).
                imageName = args[1] + "--" + args[2] + ".png";

                Log.i("LOOKING FOR", "Looking for a file with the name: " + imageName);

                // Progressing the progress bar to 50%
                publishProgress(50);

                // Calling the fileExistence method to see if a file by that name exists.
                if (fileExistence(imageName)) {
                    publishProgress(75);
                    Log.i("FOUND FILE", "Found file by the name of " + imageName);
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(imageName);
                        publishProgress(100);
                    } catch (FileNotFoundException e) {
                        Log.i("ERROR", e.toString());
                    }
                    image = BitmapFactory.decodeStream(fis);

                } else {
                    // In the case that a file by the name we created previously does not exist.
                    Log.i("NEED TO DOWNLOAD", "Need to download file by the name of " + imageName);
                    URL imageUrl = new URL(realUrl);
                    HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                    connection.connect();
                    publishProgress(75);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());

                        FileOutputStream outputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        publishProgress(100);
                    } else {
                        failedDownload = true;
                        Log.i("Error","Error downloading image");
                    }
                }
            } catch (UnknownHostException exception) {
                // Catching the case that there isn't an internet connection.
                Log.i("CAN'T CONNECT", "can't connect to the internet");
                failedDownload = true;
            } catch (Exception e) {
                // Catching the case that there was a different error.
                Log.i("ERROR", e.toString());
                failedDownload = true;
            }
            return "Done";
        }

        /**
         * fileExistence method which takes a file name and returns true or false
         * depending on whether or not it exists
         */
        public boolean fileExistence(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        /**
         * onProgressUpdate method that takes an argument of the current progress
         * and sets the progress bar to that level of progress.
         */
        public void onProgressUpdate(Integer ... args) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            progressBar.setProgress(args[0]);
        }

        /**
         * The onPostExecute method is called when doInBackground is complete.
         */
        protected void onPostExecute(String fromDoInBackground) {
            // If the global variable "failedDownload" has been set to true,
            // we display the error message TextView and the back Button.
            if(failedDownload) {
                TextView textView = (TextView) findViewById(R.id.error_text_view);
                textView.setVisibility(View.VISIBLE);

                Button button = (Button) findViewById(R.id.button_back);
                button.setVisibility(View.VISIBLE);
            } else {
                // If the global variable "failedDownload" is still false,
                // we run this code.

                // Update imageView with the image we downloaded / got from storage
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(image);

                // Showing the data of the image
                TextView longLatText = (TextView) findViewById(R.id.lon_lat_text);
                longLatText.setVisibility(View.VISIBLE);
                longLatText.setText("Date: 2014-02-01" + "\nLongitude: " + longitude + "\nLatitude: " + latitude);

                // Making the "favourite_button" button visible.
                Button favouriteButton = (Button) findViewById(R.id.favourite_button);
                favouriteButton.setVisibility(View.VISIBLE);

                // Making the "see_favourites" button visible.
                Button seeFavouritesButton = (Button) findViewById(R.id.see_favourites);
                seeFavouritesButton.setVisibility(View.VISIBLE);
            }

            // Hiding the ProgressBar.
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}