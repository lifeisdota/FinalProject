package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

/**
 * EnterDataActivity corresponds to activity_enter_data.xml
 */
public class EnterDataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * onCreate method called when EnterDataActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        // Creating a SnackBar to be displayed when this Activity is shown.
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, "Make sure to enter acceptable coordinates!", Snackbar.LENGTH_LONG);

        // Setting an action to dismiss the SnackBar.
        snackbar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        // Setting the SnackBar to show right when the activity is launched.
        snackbar.show();

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
     * Creates onClickListeners to do various things.
     */
    private void setListeners() {
        findViewById(R.id.see_the_earth_enter)
                .setOnClickListener(v ->
                        sendUrl()
                );

        findViewById(R.id.see_favourites)
                .setOnClickListener(v ->
                        startActivity(
                                new Intent(EnterDataActivity.this, ListActivity.class)
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
                    .setMessage("Enter a longitude and latitude and click enter to see the an image of the earth at those coordinates. Alternatively, click 'see favourites' to see a list of the items you have favourited.")
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
     * sendUrl method called from setListeners() when the user clicks "see_the_earth_enter" button
     */
    private void sendUrl() {

        // Getting the input for longitude
        final EditText longitudeField =  (EditText) findViewById(R.id.longitude_edit_text);
        final String longitude = longitudeField.getText().toString();

        // Getting the input for latitude
        final EditText latitudeField =  (EditText) findViewById(R.id.latitude_edit_text);
        final String latitude = latitudeField.getText().toString();

        // creating the url string with the longitude and latitude variables
        final String url = "https://api.nasa.gov/planetary/earth/imagery?lon=" + longitude + "&lat=" + latitude + "&date=2014-02-01&api_key=DEMO_KEY";

        // Creating an intent, adding information to the intent, and calling startActivity to pass the information
        // and transfer to the ImageActivity class.
        Intent i = new Intent(EnterDataActivity.this, ImageActivity.class);
        i.putExtra("url", url);
        i.putExtra("longitude", longitude);
        i.putExtra("latitude", latitude);
        startActivity(i);
    }
}