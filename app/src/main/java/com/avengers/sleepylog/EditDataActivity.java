package com.avengers.sleepylog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditDataActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    // Data from intent
    long date_l;
    long times_l[];
    long durations_l[];
    boolean naps;
    int quality;

    // calculated values
    long total_time_asleep_l;
    long total_time_in_bed_l;
    float sleep_efficiency;

    // Dates generated from intent longs
    Date date;
    Date[] times;
    Date[] durations;

    // Calculated durations
    Date total_time_asleep;
    Date total_time_in_bed;


    TextView tvDate;
    TextView[] tvTimes;
    TextView[] tvDurations;
    TextView tvNaps;
    TextView tvQuality;
    TextView tvSleepTime;
    TextView tvTimeInBed;
    TextView tvSleepEfficiency;

    private DBAdapter DBAgent;
    TextView tvDisplayTest;

    SimpleDateFormat sdfDuration = new SimpleDateFormat("HH:mm");

    SimpleDateFormat sdfDefaultTimes = new SimpleDateFormat("hh:mma");
    String[] defaultTimes = {"11:00PM","11:30PM","7:30AM","8:00AM"};

    int timePickerIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
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

        // Get data from calling Intent - for now just display
        //tvCallingActivity = (TextView) findViewById(R.id.tvECallingActivity);
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
        tvNaps = (TextView) findViewById(R.id.tvENaps);
        tvQuality = (TextView) findViewById(R.id.tvEQuality);

        tvSleepTime = (TextView) findViewById(R.id.tvSleepTime);
        tvTimeInBed = (TextView) findViewById(R.id.tvTimeinBed);
        tvSleepEfficiency = (TextView) findViewById(R.id.tvSleepEfficiency);

        times_l = new long[4];
        times = new Date[4];
        durations_l = new long[3];
        durations = new Date[3];

        Bundle extras = getIntent().getExtras();

        // Get data from bundle
        if (extras != null) {
            date_l = extras.getLong("date");
            for (int i = 0; i < 4; i++) {
                times_l[i] = extras.getLong("time" + i);
            }
            for (int i = 0; i < 3; i++) {
                durations_l[i] = extras.getLong("duration" + i);
            }
            naps = extras.getBoolean("naps");
            quality = extras.getInt("quality");
        } else {
            setDefaultDate();
            setDefaultTimes();
        }

        // calculate  total_time_asleep_l, total_time_in_bed_l, and sleep_efficiency;
        calculateData();

        // Generate Dates from longs
        date = new Date(date_l);
        for (int i = 0; i < 4; i++) {
            times[i] = new Date(times_l[i]);
        }
        for (int i = 0; i < 3; i++) {
            durations[i] = new Date(durations_l[i]);
        }
        total_time_in_bed = new Date(total_time_in_bed_l);
        total_time_asleep = new Date(total_time_asleep_l);

        displayData();
        //open database
        tvDisplayTest = (TextView)findViewById(R.id.tvDisplayTest);
        openDB();
    }

    /**
     * Set default date
     */
    public void  setDefaultDate() {
        date_l = Calendar.getInstance().getTime().getTime();
        String dateStr = DateFormat.getDateInstance().format(date);
    }

    /**
     * Set default times
     */
    public void setDefaultTimes() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for (int i = 0; i< times.length; i++) {
            try {
                Date defTime = sdfDefaultTimes.parse(defaultTimes[i]);
                Calendar defCal = Calendar.getInstance();
                defCal.setTime(defTime);
                cal.set(Calendar.HOUR_OF_DAY,defCal.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE,defCal.get(Calendar.MINUTE));
                cal.set(Calendar.SECOND,0);
                long time_in_ms = cal.getTimeInMillis();
                if ((i > 0) && (time_in_ms < times_l[i-1])) {
                    time_in_ms += 24 * 60 * 60 * 1000;
                }
                times_l[i] = time_in_ms;
                times[i] = cal.getTime();
            } catch (ParseException e) {
                //e.printStackTrace();
                times_l[i] = cal.getTimeInMillis();
            }
        }
        for (int i = 0; i< durations.length; i++) {
            durations_l[i] = 0;
        }
        naps = false;
        quality = 1;
    }

    /**
     * Calculate total_time_asleep_l, total_time_in_bed_l, and sleep_efficiency
     */
    public void calculateData() {
        long time_to_bed = times_l[0];
        long time_to_sleep = times_l[1];
        long time_wake_up = times_l[2];
        long time_out_bed = times_l[3];

        long time_to_fall_asleep = durations_l[0];
        long length_of_awakenings = durations_l[1];
        long nap_duration = durations_l[2];

        total_time_in_bed_l = time_out_bed - time_to_bed;
        total_time_asleep_l = time_wake_up - time_to_sleep - time_to_fall_asleep - length_of_awakenings;
        total_time_in_bed = new Date(total_time_in_bed_l);
        total_time_asleep = new Date(total_time_asleep_l);

        sleep_efficiency = (float) total_time_asleep_l/total_time_in_bed_l;

    }

    /**
     * Display data in TextViews
     */
    public void displayData() {
        tvDate.setText(DateFormat.getDateInstance().format(date));

        for (int i=0; i<4; i++) {
            tvTimes[i].setText(DateFormat.getTimeInstance().format(times[i]));
        }

        sdfDuration.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        for (int i=0; i<3; i++) {
            tvDurations[i].setText(sdfDuration.format(durations[i]));
        }

        tvNaps.setText(naps ? "Yes" : "No");

        tvQuality.setText("qual: " + String.valueOf(quality));

        tvTimeInBed.setText(sdfDuration.format(total_time_in_bed));
        tvSleepTime.setText(sdfDuration.format(total_time_asleep));
        tvSleepEfficiency.setText(String.format ("%.1f %%",100*sleep_efficiency));
    }

    public void onClickDate(View view) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DatePickerDialog dp = new DatePickerDialog(this, this,
                cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    public void onClickTime(View view) {
        timePickerIdx = Integer.parseInt(view.getTag().toString()) ;
        Calendar cal = Calendar.getInstance();
        if (timePickerIdx < 4) {
            cal.setTime(times[timePickerIdx]);
        } else {
            cal.setTime(durations[timePickerIdx - 4]);
        }
        TimePickerDialog tp = new TimePickerDialog(this,this,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),false);
        tp.show();
    }

    public void onClickDuration(View view) {
        timePickerIdx = Integer.parseInt(view.getTag().toString()) ;
        TimePickerDialog tp = new TimePickerDialog(this,this,0,0,true);
        tp.show();
    }

    public void onClickNaps(View view) {

    }

    public void onClickSleepQuality(View view) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day,0,0);
        date_l = cal.getTimeInMillis();
        date = new Date(date_l);
        setDefaultTimes();
        calculateData();
        displayData();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        if (timePickerIdx < 4) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
            cal.set(Calendar.MINUTE,minute);
            cal.set(Calendar.SECOND,0);
            long time_in_ms = cal.getTimeInMillis();
            if ((timePickerIdx > 0) && (time_in_ms < this.times_l[timePickerIdx-1])) {
                time_in_ms += 24 * 60 * 60 * 1000;
            }
            this.times_l[timePickerIdx] = time_in_ms;
            this.times[timePickerIdx] = new Date(time_in_ms);
        } else {
            long duration_in_ms = ((hourOfDay * 60) + minute) * 60 * 1000;
            this.durations_l[timePickerIdx - 4] = duration_in_ms;
            this.durations[timePickerIdx - 4] = new Date(duration_in_ms);
        }
        calculateData();
        displayData();
    }

    public void openDB() {
        DBAgent =  DBAgent.getInstance(this);
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

    /**
     * Goes to display activity
     * @param view Button Done
     */
    public void onEditDataDone(View view) {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);
        //Cursor cursor = DBAgent.getAll();
        //displayRecord(cursor);
        this.finish();
    }

    /**
     * Enters data into database.
     * @param view Button Enter Data in DB
     */
    public void enterDataInDatabase(View view){
        long time_to_bed = times_l[0];
        long time_to_sleep = times_l[1];
        long time_to_wake_up = times_l[2];
        long time_out_bed = times_l[3];

        long asleep = durations_l[0];
        long awake = durations_l[1];
        long nap_duration = durations_l[2];
        // check if a row exists
        //String stringDate = DateFormat.getDateInstance().format(date_l);
        String stringDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(date_l));
        if(!DBAgent.checkIfRowExists(stringDate)) {
            //Send result to database
            long rowId = DBAgent.insertRow(stringDate, time_to_bed, time_to_sleep, time_to_wake_up,
                    time_out_bed, asleep, awake, nap_duration,
                    naps, quality, total_time_asleep_l, total_time_in_bed_l, sleep_efficiency);
            if (rowId > 0) {
                tvDisplayTest.setText("Insert succeeded. RowId=" + rowId);
            } else {
                tvDisplayTest.setText("Insert failed. stringDate: " + stringDate);
            }
        }else{
            tvDisplayTest.setText(R.string.already_in_db);
        }
    }

    /**
     * Tests database table.
     * @param cursor
     */
    public  void displayRecord(Cursor cursor){
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            String output = "";
            do {
                String date = cursor.getString(DBAdapter.COL_DATE);
                //String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(date));
                Long time_to_bed = cursor.getLong(DBAdapter.COL_TIME_TO_BED);
                String timeBedString = new SimpleDateFormat("HH:mm").format(new Time(time_to_bed));
                //Long time_to_sleep = cursor.getLong(DBAdapter.COL_TIME_TO_SLEEP);

                output += "date: " + date + " time: " + timeBedString + "\n";
            } while (cursor.moveToNext());
            tvDisplayTest.setText(output);
            cursor.close();
        } else {
            tvDisplayTest.setText("The database is empty.");
        }
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

        if (id == R.id.nav_main) {
            Intent newAct = new Intent(this, MainActivity.class);
            startActivity(newAct);
        } else if (id == R.id.nav_edit_data) {
            // Do nothing
        } else if (id == R.id.nav_display_data) {
            Intent newAct = new Intent(this, DisplayDataActivity.class);
            startActivity(newAct);
        } else if (id == R.id.nav_help) {
            Intent newAct = new Intent(this, NewHelpActivity.class);
            startActivity(newAct);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
