package com.avengers.sleepylog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
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
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int MAIN_ACTIVITY_CODE = 1;

    Date date;
    TextView tvDate;
    TextView tvSleepButtonState;
    Button btnSleep;
    Button btnBack;
    int sleepButtonState = 0;
    String sleepButtonStrings[];
    TextView[] tvTimes;
    Date[] times;


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

        // For testing, long clock on date will launch DatePickerDialog
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        date = calendar.getTime();
                        String dateStr = DateFormat.getDateInstance().format(date);
                        tvDate.setText(dateStr);
                    }
                } , year, month, day);
                dpDialog.show();
                return true;
            }
        });

        tvSleepButtonState = (TextView) findViewById(R.id.tvSleepButtonState);
        sleepButtonState = 0;
        String bedtime = getString(R.string.time_to_bed);
        String sleepTime = getString(R.string.time_to_sleep);
        String wakeUpTime = getString(R.string.time_to_wake);
        String outBedTime = getString(R.string.time_out_of_bed);;

        sleepButtonStrings = new String[] {bedtime, sleepTime, wakeUpTime, outBedTime};
        btnSleep = (Button) findViewById(R.id.btnSleep);
        btnSleep.setText(sleepButtonStrings[sleepButtonState]);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetSleepButtonState();
                return true;
            }
        });
        TextView tvTime0 = (TextView) findViewById(R.id.tvTime0);
        TextView tvTime1 = (TextView) findViewById(R.id.tvTime1);
        TextView tvTime2 = (TextView) findViewById(R.id.tvTime2);
        TextView tvTime3 = (TextView) findViewById(R.id.tvTime3);
        tvTimes = new TextView[] {tvTime0,tvTime1,tvTime2,tvTime3};
        times = new Date[4];

        resetSleepButtonState();
    }

    public void resetSleepButtonState() {
        date = Calendar.getInstance().getTime();
        String dateStr = DateFormat.getDateInstance().format(date);
        tvDate.setText(dateStr);
        sleepButtonState = 0;
        tvSleepButtonState.setText("state: " + sleepButtonState);
        btnSleep.setText(sleepButtonStrings[sleepButtonState]);
        btnBack.setVisibility(View.INVISIBLE);
        for (int i=0; i<4; i++) {
            tvTimes[i].setText("");
            tvTimes[i].setVisibility(View.INVISIBLE);
        }
    }

    public void onSleepButton(View view) {
        Date time = Calendar.getInstance().getTime();
        String timeStr = DateFormat.getTimeInstance().format(time);
        times[sleepButtonState] = time;
        tvTimes[sleepButtonState].setText(sleepButtonStrings[sleepButtonState] + ": " + timeStr);
        tvTimes[sleepButtonState].setVisibility(View.VISIBLE);
        sleepButtonState++;
        tvSleepButtonState.setText("state: " + sleepButtonState);

        if (sleepButtonState == 4) {
            Intent intent = new Intent(this,QuestionsActivity.class);
            intent.putExtra("caller","MainActivity");
            intent.putExtra("date",date.getTime());
            intent.putExtra("time0",times[0].getTime());
            intent.putExtra("time1",times[1].getTime());
            intent.putExtra("time2",times[2].getTime());
            intent.putExtra("time3",times[3].getTime());
            startActivityForResult(intent,MAIN_ACTIVITY_CODE);
        } else {
            btnSleep.setText(sleepButtonStrings[sleepButtonState]);
            btnBack.setVisibility(View.VISIBLE);
        }
    }

    public void onBackButton(View view) {
        if (sleepButtonState > 0) {
            sleepButtonState--;
            tvTimes[sleepButtonState].setVisibility(View.INVISIBLE);
            tvSleepButtonState.setText("state: " + sleepButtonState);
            btnSleep.setText(sleepButtonStrings[sleepButtonState]);
            if (sleepButtonState == 0) {
                resetSleepButtonState();
            }
        }
    }

    public void onDisplayDataButton(View view) {
        Intent intent = new Intent(this,DisplayDataActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAIN_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
               resetSleepButtonState();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                sleepButtonState = 3;
                tvSleepButtonState.setText("state: " + sleepButtonState);
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
