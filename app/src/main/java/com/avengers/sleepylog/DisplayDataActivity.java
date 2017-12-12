package com.avengers.sleepylog;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Display
 */
public class DisplayDataActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
         DatePickerDialog.OnDateSetListener {

    private TextView tvDisplay;
    private DBAdapter DBAgent;
    private String pickedDate;
    private TextView tvcalendarDate1;
    //private TextView tvcalendarDate2;

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

        tvcalendarDate1 = findViewById(R.id.calendarDate1);
       // tvcalendarDate2 = findViewById(R.id.calendarDate2);
        tvDisplay = (TextView)findViewById(R.id.tvDisplay);
        final TextView tvPickStyle = (TextView)findViewById(R.id.tvTitle);
        //final Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        //dropdown.setVisibility(View.VISIBLE);
        //tvPickStyle.setVisibility(View.VISIBLE);

        String[] items = new String[]{
                "Time to bed",
                "Time to sleep",
                "Time to wake up",
                "Time out of bed",
                "How long to fall asleep",
                "Take nap",
                "Rate your sleep quality"
        };
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //dropdown.setAdapter(adapter);

        openDB();


    }

    /**
     *
     * @param view Button Pick a date
     */
    public void onClickChooseDate(View view) {
        Calendar cal = Calendar.getInstance();
        //cal.setTime(date);
        DatePickerDialog dp = new DatePickerDialog(this, this,
                cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,dayOfMonth,0,0);

        pickedDate =  new SimpleDateFormat("MM/dd/yyyy").format(new Date(cal.getTimeInMillis()));
        tvcalendarDate1.setText(pickedDate);
        showEntryByDate();
    }

    public void openDB() {
        DBAgent = DBAgent.getInstance(this);
        DBAgent.open();
    }

    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    public void closeDB() {
        DBAgent.close();
    }

    /**
     *
     * @param view Button Clear
     */
    public void onClearClicked(View view) {
        DBAgent.deleteAll();
    }

    public void showAllEntries(View view){
        Cursor cursor = DBAgent.getAll();
        displayRecord(cursor);
    }

    /**
     * Tests database table.
     * @param cursor
     */
    public  void displayRecord(Cursor cursor){
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            StringBuilder output = new StringBuilder();
            do {
                String date = cursor.getString(DBAdapter.COL_DATE);
                Long time_to_bed = cursor.getLong(DBAdapter.COL_TIME_TO_BED);
                String timeBedString = new SimpleDateFormat("HH:mm").format(new Time(time_to_bed));
                //Long time_to_sleep = cursor.getLong(DBAdapter.COL_TIME_TO_SLEEP);


                output.append("date: ").append(date).append(" time: ").append(timeBedString).append("\n");
            } while (cursor.moveToNext());
            tvDisplay.setText(output.toString());
            cursor.close();
        } else {
            tvDisplay.setText("The database is empty.");
        }
    }

    /**
     * retrieve records by date
     */
    public void showEntryByDate(){
        Cursor cursor = DBAgent.getRowByPrimaryKey(pickedDate);
        //displayRecord(cursor);
        StringBuilder output = new StringBuilder();
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String date = cursor.getString(DBAdapter.COL_DATE);

            Long time_to_bed = cursor.getLong(DBAdapter.COL_TIME_TO_BED);
            String timeBedString = new SimpleDateFormat("HH:mm").format(new Time(time_to_bed));
            Long time_to_sleep = cursor.getLong(DBAdapter.COL_TIME_TO_SLEEP);
            String timeString = new SimpleDateFormat("HH:mm").format(new Time(time_to_sleep));
            Long total_time_asleep = cursor.getLong(DBAdapter.COL_TOTAL_TIME_ASLEEP);
            String timeAsleepString = new SimpleDateFormat("HH:mm").format(new Time(total_time_asleep));
            Long total_time_in_bed = cursor.getLong(DBAdapter.COL_TOTAL_TIME_IN_BED);
            String timeInBedString = new SimpleDateFormat("HH:mm").format(new Time(total_time_in_bed));

            output.append("date: ").append(date).append("\ntime to bed: ").append(timeBedString);
            output.append("\ntime to sleep: ").append(timeString).append("\ntotal time asleep: ").append(timeAsleepString);
            output.append("\ntotal time in bed: ").append(timeInBedString);

            tvDisplay.setText(output);
        } else {
            tvDisplay.setText("For picked date: " + tvcalendarDate1.getText().toString() + " entry is not found");
        }
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

        if (id == R.id.nav_main) {
            Intent newAct = new Intent(this, MainActivity.class);
            startActivity(newAct);
        } else if (id == R.id.nav_edit_data) {
            Intent newAct = new Intent(this, EditDataActivity.class);
            startActivity(newAct);
        } else if (id == R.id.nav_display_data) {
            // Do nothing
        } else if (id == R.id.nav_help) {
            Intent newAct = new Intent(this, NewHelpActivity.class);
            startActivity(newAct);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
