package com.avengers.sleepylog;

import android.app.DatePickerDialog;
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
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvTitle, tvDisplay;
    Button btnPicker;
    Spinner dropdown;
    DBAdapter DBAgent;
    int day, month, year;

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


        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDisplay = (TextView) findViewById(R.id.tvDisplay);
        btnPicker = (Button)findViewById(R.id.btnPicker);
        btnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DATE);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                //tvDate.setText((month + 1) +"/" + day + "/" + year);
                tvDisplay.setText("");

                DatePickerDialog myDateDialog = new DatePickerDialog(DisplayDataActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year = i;
                        month = i1;
                        day = i2;
                        tvDisplay.setText((month + 1) +"/" + day + "/" + year);
                    }
                }, year, month, day);
                myDateDialog.setTitle(getResources().getString(R.string.dateAlertDialogTitle));
                myDateDialog.setMessage(getResources().getString(R.string.dateAlertDialogMessage));
                myDateDialog.show();
            }
        });

        //dropdown = (Spinner) findViewById(R.id.spinner);
        //dropdown.setVisibility(View.VISIBLE);
        //tvPickStyle.setVisibility(View.VISIBLE);

    }
/**
        String[] items = new String[]{
                "None",
                "Time to bed",
                "Time to sleep",
                "Time to wake up",
                "Time out of bed",
                "How long to fall asleep",
                "How long to wake up",
                "Take nap",
                "Rate your sleep quality"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Cursor cursor;
        //dropdown = (Spinner)findViewById(R.id.spinner);
        switch (dropdown.getSelectedItemPosition()) {
            case 0:
                tvDisplay.setText(dropdown.getSelectedItemPosition());
                //tvDisplay.setText("haha");
                //Cursor cursor = DBAgent.getAll();
                //displayRecords(cursor);
                break;
            case 1:
                tvDisplay.setText("haha");
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[1]);
                break;
            case 2:
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[2]);
                break;
            case 3:
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[3]);
                break;
            case 4:
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[4]);
                break;
            case 5:
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[5]);
                break;
            case 6:
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[6]);
                break;
            case 7:
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[7]);
                break;
            case 8:
                cursor = DBAgent.getAll();
                displayRecord(cursor, items[8]);
                break;
        }
        //openDB();

    }

    public  void displayRecord(Cursor cursor, String item){
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            String output = "";
            tvDisplay.setText("Date" + "\t\t" + item);
            String dataString = "";
            do {
                Long date = cursor.getLong(DBAdapter.COL_DATE);
                String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(date));
                switch (item) {
                    case "Time to bed":
                        Long time_to_bed = cursor.getLong(DBAdapter.COL_TIME_TO_BED);
                        dataString = new SimpleDateFormat("HH:mm").format(new Time(time_to_bed));
                        break;
                    case "Time to sleep":
                        Long time_to_sleep = cursor.getLong(DBAdapter.COL_TIME_TO_SLEEP);
                        dataString = new SimpleDateFormat("HH:mm").format(new Time(time_to_sleep));
                        break;
                    case "Time to wake up":
                        Long time_to_wake_up = cursor.getLong(DBAdapter.COL_TIME_TO_WAKE_UP);
                        dataString = new SimpleDateFormat("HH:mm").format(new Time(time_to_wake_up));
                        break;
                    case "Time out of bed":
                        Long time_out_bed = cursor.getLong(DBAdapter.COL_TIME_OUT_BED);
                        dataString = new SimpleDateFormat("HH:mm").format(new Time(time_out_bed));
                        break;
                    case "How long to fall asleep":
                        Long asleep = cursor.getLong(DBAdapter.COL_ASLEEP);
                        dataString  = new SimpleDateFormat("HH:mm").format(new Time(asleep));
                        break;
                    case "How long to wake up":
                        Long awake = cursor.getLong(DBAdapter.COL_AWAKE);
                        dataString = new SimpleDateFormat("HH:mm").format(new Time(awake));
                        break;
                    case "Take nap":
                        String naps = cursor.getString(DBAdapter.COL_NAP);
                        Long duration_nap = cursor.getLong(DBAdapter.COL_DURATION_NAP);
                        dataString = new SimpleDateFormat("HH:mm").format(new Time(duration_nap));
                        break;
                    case "Rate your sleep quality":
                        dataString = String.valueOf(cursor.getInt(DBAdapter.COL_QUALITY));
                        break;
                }

                output += date + "\t\t" + dataString + "\n";
            } while (cursor.moveToNext());
            tvDisplay.setText(output);
            cursor.close();
        } else {
            tvDisplay.setText("The database is empty.");
        }
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
    }**/


    public void displayRecords(Cursor cursor) {


    }

    public void onClearClicked(View view) {

        DBAgent.deleteAll();
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
