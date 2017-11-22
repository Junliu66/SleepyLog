package com.avengers.sleepylog;

import android.content.Intent;
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
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnSleep;
    Button btnBack;
    int sleepButtonState = 0;
    String sleepButtonStrings[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Above created by Android Studio Navigation Drawer Activity template
        sleepButtonState = 0;
        String bedtime = getString(R.string.time_to_bed);
        String sleepTime = getString(R.string.time_to_sleep);
        String wakeUpTime = getString(R.string.time_to_wake);
        String outBedTime = getString(R.string.time_out_of_bed);
        sleepButtonStrings = new String[] {bedtime, sleepTime, wakeUpTime, outBedTime};
        btnSleep = (Button) findViewById(R.id.btnSleep);
        btnSleep.setText(sleepButtonStrings[sleepButtonState]);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sleepButtonState = 0;
                btnSleep.setText(sleepButtonStrings[sleepButtonState]);
                btnBack.setVisibility(View.INVISIBLE);
                return true;
            }
        });

    }

    public void onSleepButton(View view) {
        if (sleepButtonState == 3) {
            Intent questionsIntent = new Intent(this,QuestionsActivity.class);
            // questionsIntent.putExtras(...);
            startActivity(questionsIntent);
        } else {
            sleepButtonState++;
            btnSleep.setText(sleepButtonStrings[sleepButtonState]);
            btnBack.setVisibility(View.VISIBLE);
        }
    }

    public void onBackButton(View view) {
        if (sleepButtonState > 0) {
            sleepButtonState--;
            btnSleep.setText(sleepButtonStrings[sleepButtonState]);
            if (sleepButtonState == 0) {
                btnBack.setVisibility(View.INVISIBLE);
            }
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
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
