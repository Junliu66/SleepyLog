package com.avengers.sleepylog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Display
 */
public class DisplayDataActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvDisplay;
    private DBAdapter DBAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Above created by Android Studio Navigation Drawer Activity template


        tvDisplay = (TextView)findViewById(R.id.tvDisplay);
        final TextView tvPickStyle = (TextView)findViewById(R.id.tvTitle);
        final Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        dropdown.setVisibility(View.VISIBLE);
        tvPickStyle.setVisibility(View.VISIBLE);

        String[] items = new String[]{
                "Time to bed",
                "Time to sleep",
                "Time to wake up",
                "Time out of bed",
                "How long to fall asleep",
                "Take nap",
                "Rate your sleep quality"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        //openDB();


    }

    public void showData(View v) {
        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        switch (dropdown.getSelectedItemPosition()) {
            case 0:
                tvDisplay.setText("haha");
                //Cursor cursor = DBAgent.getAll();
                //displayRecords(cursor);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    public void openDB() {
        DBAgent = new DBAdapter(this);
        DBAgent.open();
    }

    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    public void closeDB() {
        DBAgent.close();
    }

    public void onClearClicked(View view) {

        DBAgent.deleteAll();
    }


    public void displayRecords(Cursor cursor) {

    }

    public void onDisplayDataDone(View view) {
        this.finish();
    }

    // Below created by Android Studio Navigation Drawer Avtivity template
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
