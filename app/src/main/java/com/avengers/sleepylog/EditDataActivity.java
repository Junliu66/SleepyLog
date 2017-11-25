package com.avengers.sleepylog;

import android.app.Activity;
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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EditDataActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Date date;
    TextView tvDate;
    Date[] times;
    TextView[] tvTimes;
    Date[] durations;
    TextView[] tvDurations;
    boolean naps;
    int quality;

    String callingActivity;
    TextView tvCallingActivity;

    SimpleDateFormat sdfDuration = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
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

        // Get data from calling Intent - for now just display
        tvCallingActivity = (TextView) findViewById(R.id.tvECallingActivity);
        tvDate = (TextView) findViewById(R.id.tvEDate);
        TextView tvTime0 = (TextView) findViewById(R.id.tvETime0);
        TextView tvTime1 = (TextView) findViewById(R.id.tvETime1);
        TextView tvTime2 = (TextView) findViewById(R.id.tvETime2);
        TextView tvTime3 = (TextView) findViewById(R.id.tvETime3);
        tvTimes = new TextView[] {tvTime0,tvTime1,tvTime2,tvTime3};
        TextView tvDuration0 = (TextView) findViewById(R.id.tvEDuration0);
        TextView tvDuration1 = (TextView) findViewById(R.id.tvEDuration1);
        TextView tvDuration2 = (TextView) findViewById(R.id.tvEDuration2);
        tvDurations = new TextView[] {tvDuration0,tvDuration1,tvDuration2};
        TextView tvNaps = (TextView) findViewById(R.id.tvENaps);
        TextView tvQuality = (TextView) findViewById(R.id.tvEQuality);

        times = new Date[4];
        durations = new Date[3];

        Bundle extras = getIntent().getExtras();
        callingActivity = extras.getString("caller");
        tvCallingActivity.setText(tvCallingActivity.getText() + " " + callingActivity);
        date = new Date(extras.getLong("date"));
        tvDate.setText(DateFormat.getDateInstance().format(date));

        for (int i=0; i<4; i++) {
            times[i] = new Date(extras.getLong("time"+i));
            tvTimes[i].setText(DateFormat.getTimeInstance().format(times[i]));
        }

        sdfDuration.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        for (int i=0; i<3; i++) {
            durations[i] = new Date(extras.getLong("duration"+i));
            tvDurations[i].setText(sdfDuration.format(durations[i]));
        }

        naps = extras.getBoolean("naps");
        tvNaps.setText(String.valueOf(naps));

        quality = extras.getInt("quality");
        tvQuality.setText("qual: " + String.valueOf(quality));
    }

    public void onEditDataDone(View view) {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);
        this.finish();
    }

    public void onEditDataBack(View view) {
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
        getMenuInflater().inflate(R.menu.edit_data, menu);
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
