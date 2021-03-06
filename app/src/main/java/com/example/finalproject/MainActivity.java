package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

/**
 * MainActivity is called when the app first launches.
 * It corresponds to activity_main.xml
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer = null;

    /**
     * onCreate is called when MainActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Getting the users name from shared preferences, if nothing is stored,
        // setting the String "usersName" to an empty string.
        SharedPreferences prefs = getSharedPreferences("editor", MODE_PRIVATE);
        String usersName = prefs.getString("usersName", "");

        // Adding the users name to the display.
        TextView textView = (TextView)findViewById(R.id.name_goes_here);
        String greeting = this.getResources().getString(R.string.hi);
        textView.setText(greeting + " " + usersName + "!");

        if(usersName != "") {
            textView.setVisibility(View.VISIBLE);
        }

        // Calling the setListeners function to make sure we are listening for input.
        setListeners();
    }

    /**
     * setListeners called from onCreate.
     * Creates onClickListeners to move to different activities.
     */
    private void setListeners() {
        findViewById(R.id.home_screen_button)
                .setOnClickListener(v ->
                        startActivity(
                                new Intent(MainActivity.this, EnterDataActivity.class)
                        )
                );

        findViewById(R.id.see_favourites)
                .setOnClickListener(v ->
                        startActivity(
                                new Intent(MainActivity.this, ListActivity.class)
                        )
                );

        findViewById(R.id.enter_name)
                .setOnClickListener(v -> {

                            // Getting the text from the EditText view.
                            final EditText usersNameField =  (EditText) findViewById(R.id.usersName);
                            final String usersName = usersNameField.getText().toString();

                            // Saving the name to shared preferences.
                            SharedPreferences.Editor editor = getSharedPreferences("editor", MODE_PRIVATE).edit();
                            editor.putString("usersName", usersName);
                            editor.apply();

                            // Creating a SnackBar to notify the user that we have received
                            // the name they entered.
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar snackbar = Snackbar.make(parentLayout, R.string.remember, Snackbar.LENGTH_LONG);
                            snackbar.setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
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
                    .setMessage("Click one of the buttons to navigate to the desired page.")
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