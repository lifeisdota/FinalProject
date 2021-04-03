package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Importing the Message class to add values
 */
import com.example.finalproject.models.Message;
import com.google.android.material.navigation.NavigationView;

/**
 * MainActivity is called when the app first launches.
 * It corresponds to activity_main.xml
 */
public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Creating an object of the Messages class we created in /models/Message.
    // This will store the data of our listItems.
    private final List<Message> listItems = new ArrayList<>();

    // Creating a MessageListAdapter object.
    MessageListAdapter messageListAdapter = new MessageListAdapter();

    // Creating a SQLiteDatabase object to access our database.
    SQLiteDatabase db;

    // Creating these global variables to access throughout our class.
    String date = null;
    String longitude = null;
    String latitude = null;
    String imageName = null;

    /**
     * onCreate is called when ListActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calling our loadDataFromDatabase method to load the data from the database.
        loadDataFromDatabase();
        setContentView(R.layout.activity_list);

        // Checking if an intent was passed from the previous activity.
        if(getIntent().getExtras() != null) {
            // If there was an intent passed, we get the data from the intent.
            Intent intent = getIntent();
            date = intent.getExtras().getString("date");
            longitude = intent.getExtras().getString("longitude");
            latitude = intent.getExtras().getString("latitude");
            imageName = intent.getExtras().getString("image_name");

            // Here we pass the data to our addMessage method.
            addMessage(date, longitude, latitude, imageName);
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
                    .setMessage("Here you will see a list of your favouited images of the earth. Tap an entry in the list to see the image!")
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
     * setListeners called from onCreate.
     * Creates onClickListeners to do various things.
     */
    private void setListeners() {
        // Creating ListView object called messageList and connecting it to our list.
        ListView messageList = findViewById(R.id.list);
        messageList.setAdapter(messageListAdapter);

        // Waiting for a message list item to be clicked.
        messageList.setOnItemClickListener((list, view, position, id) -> {

            // Creating a Bundle to pass data to the next activity.
            Bundle dataToPass = new Bundle();
            dataToPass.putString("date", listItems.get(position).getDate());
            dataToPass.putString("longitude", listItems.get(position).getLongitude());
            dataToPass.putString("latitude", listItems.get(position).getLatitude());
            dataToPass.putString("image_name", listItems.get(position).getImageName());

            // Keeping this recycled code for later.

//            if(isTablet) {
//                DetailsFragment detailsFragment = new DetailsFragment();
//                detailsFragment.setArguments(dataToPass);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.frameLayout, detailsFragment);
//                fragmentTransaction.commit();
//            }
//            else {

            // Creating an Intent object called myIntent to transfer to the ViewListActivity.
            Intent myIntent = new Intent(ListActivity.this, ViewListActivity.class);

            // Adding our data to the intent.
            myIntent.putExtras(dataToPass);

            // Transferring to the ViewListActivity while passing the data we added.
            startActivity(myIntent);
//            }
        });

        // Keeping this recycled code for later.

        // Set long click listener for each item in the list.
        messageList.setOnItemLongClickListener(((parent, view, position, id) -> {

            // Creating an AlertDialog when the user long clicks an item in the list.
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.
                    // Setting the title.
                            setTitle(getString(R.string.delete))

                    // Setting a body message.
                    .setMessage(
                            getString(R.string.image) + " " + listItems.get(position).getId() + "\n" +
                                    getString(R.string.database) + " " + db.getVersion()
                    )
                    // On the event that the user clicks yes, this code will run.
                    .setPositiveButton(getString(R.string.yes), (click, arg) -> {

                        // Deleting the list item from the database.
                        deleteMessageFromDB(listItems.get(position).getId());

                        // Deleting the list item from our listItems array
                        listItems.remove(position);

                        // Updating the list on the users screen.
                        messageListAdapter.notifyDataSetChanged();
                    })

                    // In the event that the user clicks no, this code will run, nothing will happen.
                    .setNegativeButton(R.string.no, (click, arg) -> {
                    })
                    .show();

            // Remember to return true or both the longClick and Click events will trigger.
            return true;
        }));
    }

    /**
     * DeleteMessageFromDB deletes a column based on the column number passed.
     */
    protected void deleteMessageFromDB(long m) {
        Log.d("M", "m: " + m);
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(m)});
    }

    /**
     * addMessage method takes data about the image and inserts it into the
     * database and adds it to the listItems array.
     */
    private void addMessage(String date, String longitude, String latitude, String imageName) {

        // Creating an object of ContentValues called newRowValues.
        ContentValues newRowValues = new ContentValues();

        // Adding the data to the passed to the newRowValues object.
        newRowValues.put(MyOpener.COL_DATE, date);
        newRowValues.put(MyOpener.COL_LONGITUDE, longitude);
        newRowValues.put(MyOpener.COL_LATITUDE, latitude);
        newRowValues.put(MyOpener.COL_IMAGE_NAME, imageName);

        // Inserting the data added into the newRowValues object into the database.
        long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

        // Adding the data passed to the listItems array via an object of the Message class we created.
        Message message = new Message(date, longitude, latitude, imageName, newId);
        listItems.add(message);

        // Updating the list.
        messageListAdapter.notifyDataSetChanged();
    }

    /**
     * loadDataFromDatabase method is called from onCreate.
     * The purpose of this method is to add the database data to the ListItems array
     * so that it can be displayed in the list.
     */
    private void loadDataFromDatabase() {

        // Creating an object of the MyOpener class called dbOpener.
        MyOpener dbOpener = new MyOpener(this);

        // Setting the db variable.
        db = dbOpener.getWritableDatabase();

        // Creating a String array of all the columns in the database.
        String [] columns = {MyOpener.COL_DATE, MyOpener.COL_LONGITUDE, MyOpener.COL_LATITUDE, MyOpener.COL_IMAGE_NAME, MyOpener.COL_ID };

        // Query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        // Getting the indexes of the various columns.
        int dateColIndex = results.getColumnIndex(MyOpener.COL_DATE);
        int longitudeColIndex = results.getColumnIndex(MyOpener.COL_LONGITUDE);
        int latitudeColIndex = results.getColumnIndex(MyOpener.COL_LATITUDE);
        int imageNameColIndex = results.getColumnIndex(MyOpener.COL_IMAGE_NAME);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        // So long as there is a next result in the results variable, this will be true.
        while(results.moveToNext())
        {
            // Getting the given variables depending on where we are in the results.
            String date = results.getString(dateColIndex);
            String longitude = results.getString(longitudeColIndex);
            String latitude = results.getString(latitudeColIndex);
            String imageName = results.getString(imageNameColIndex);
            long id = results.getLong(idColIndex);

            // Add the current values to the listItems array via the an object of the Message
            // class we created.
            listItems.add(new Message(date, longitude, latitude, imageName, id));
        }
    }

    /**
     * MessageListAdapter class that extends BaseAdapter. Here we
     * deal with the details of our list.
     */
    private class MessageListAdapter extends BaseAdapter {

        /**
         * getCount method that returns the size of the listItems array.
         */
        @Override
        public int getCount() {
            return listItems.size();
        }

        /**
         * getItem method that returns a given item based on the position
         * that is passed.
         */
        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        /**
         * getItemId method that returns a given id based on the position
         * that is passed.
         */
        @Override
        public long getItemId(int position) {
            Message selectedMessage = listItems.get(position);
            return selectedMessage.getId();
        }

        /**
         * getItemViewType method that returns our row.xml layout.
         */
        @Override
        public int getItemViewType(int position) {
            return R.layout.row;
        }

        /**
         * getView method that returns a new view based on the data passed.
         */
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(getItemViewType(position), parent, false);

            TextView messageText = newView.findViewById(R.id.message);

            String myText = "image " + listItems.get(position).getId() +
                    "\ndate: " + listItems.get(position).getDate() +
                    "\nlon: " + listItems.get(position).getLongitude() +
                    "\nlat: " + listItems.get(position).getLatitude();

            messageText.setText(myText);

            return newView;
        }
    }
}