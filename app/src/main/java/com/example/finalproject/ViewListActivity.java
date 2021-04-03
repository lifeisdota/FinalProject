package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * ViewListActivity class that extends AppCompatActivity and implements
 * NavigationView.OnNavigationItemSelectedListener.
 * This class helps display a favourited image as well as its details.
 */
public class ViewListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Global variables set here to be accessible anywhere in the class.
    String date = null;
    String longitude = null;
    String latitude = null;
    String imageName = null;

    /**
     * onCreate is called when ViewListActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        // Checking if an intent was passed from the previous activity.
        if (getIntent().getExtras() != null) {
            // If there was an intent passed, we get the data from the intent.
            Intent intent = getIntent();
            date = intent.getExtras().getString("date");
            longitude = intent.getExtras().getString("longitude");
            latitude = intent.getExtras().getString("latitude");
            imageName = intent.getExtras().getString("image_name");

            // Creating a TextView object called detailsText so we can set the text
            // of the 'details_text' TextView
            TextView detailsText = (TextView) findViewById(R.id.details_text);

            // Setting the text with the information we got from the intent.
            detailsText.setText("Date: " + date + "\nLongitude: " + longitude + "\nLatitude: " + latitude);

            // Getting the image from storage using the imageName that was passed
            // in the intent.
            File imgFile = new File (getFilesDir(), imageName);

            // Checking if the image exists.
            if (imgFile.exists()) {
                //If it does exist, creating a Bitmap image from it.
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = BitmapFactory.decodeStream(new FileInputStream(imgFile));
                } catch (FileNotFoundException e) {
                    Log.i("ERROR", e.toString());
                }

                // Adding the image to the view.
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(bitmapImage);
            }
        }

        // Calling the setListeners function to make sure we are listening for input.
        setListeners();

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
     * setListeners called from onCreate.
     * Creates an onClickListener to change Activites if the 'back_button' is clicked.
     */
    private void setListeners() {
        findViewById(R.id.button_back)
                .setOnClickListener(v ->
                        startActivity(
                                new Intent(ViewListActivity.this, ListActivity.class)
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
                    .setMessage("If you accessed this page from the navigation drawer, not much will be available. Try accessing this page from " +
                            "the ListActivity page. Here you will see an image of the earth of the coordinates originally entered when you favourited " +
                            "the image, as well as the date the image was taken and coordinates of the image. Click 'back' to go back t the ListActivity page.")
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
}