package com.avengers.sleepylog;

import android.app.Activity;
import android.app.TimePickerDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class QuestionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener{

    static final int QUESTION_ACTIVITY_CODE = 2;
    Date date;
    Date[] times;
    String callingActivity;
    Spinner spnSleepQuality;
    RadioGroup rgNap;

    int durationsIdx = 0;
    long durations[] = new long[3];
    TextView tvDurations[] = new TextView[3];
    boolean naps = false;
    long duration_00_00_l;
    Date duration_00_00;

    SimpleDateFormat sdfDuration = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
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

        // Get data from calling Intent
        times = new Date[4];
        Bundle extras = getIntent().getExtras();
        callingActivity = extras.getString("caller");
        date = new Date(extras.getLong("date"));
        for (int i=0; i<4; i++) {
            times[i] = new Date(extras.getLong("time"+i));
        }

        // Initialize spinners
        spnSleepQuality = (Spinner)findViewById(R.id.spinnerSleepQuality);

        // Time picker results:
        sdfDuration.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        durationsIdx = 0;
        tvDurations = new TextView[] {
                (TextView) findViewById(R.id.tvSetFallAsleep),
                (TextView) findViewById(R.id.tvSetTimeAwakenings),
                (TextView) findViewById(R.id.tvSetTimeNaps)};
        /*
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        calendar.setTimeInMillis(0);
        duration_00_00_l = calendar.getTimeInMillis();
        duration_00_00 = calendar.getTime();
        for (int i=0; i<3;i++) {
            this.durations[i] = duration_00_00_l;
            this.tvDurations[i].setText(sdfDuration.format(duration_00_00) + " : " + String.valueOf(duration_00_00_l));
        }
        */
        duration_00_00_l = 0;
        duration_00_00 = new Date(duration_00_00_l);
        for (int i=0; i<3;i++) {
            this.durations[i] = 0;
            //this.tvDurations[i].setText(sdfDuration.format(duration_00_00) + " : " + String.valueOf(duration_00_00_l));
            this.tvDurations[i].setText(sdfDuration.format(duration_00_00));
        }

        naps = false;

        rgNap = (RadioGroup) findViewById(R.id.rgNap);
        rgNap.clearCheck();

        rgNap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                Button btn = (Button) findViewById(R.id.btnSetTimeNaps);
                TextView tv = (TextView) findViewById((R.id.tvSetTimeNaps));
                if ((rb != null) && (checkedId > -1)) {
                    if (checkedId == R.id.rbNapYes) {
                        naps = true;
                        btn.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.VISIBLE);
                    }
                    if (checkedId == R.id.rbNapNo) {
                        naps = false;
                        btn.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.INVISIBLE);
                        durationsIdx = Integer.parseInt(btn.getTag().toString()) ;
                        durations[durationsIdx] = 0;
                        tvDurations[durationsIdx].setText("00:00");
                    }
                }
            }
        });
    }


    public void onSetTimeDuration(View view) {
        durationsIdx = Integer.parseInt(view.getTag().toString()) ;
        TimePickerDialog tp = new TimePickerDialog(this,this,0,0,true);
        tp.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        /*
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(1970,0, 1, hourOfDay,minute,0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.durations[durationsIdx] = calendar.getTimeInMillis();
        Date timeSelected = calendar.getTime();
        //DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        //df.setTimeZone(TimeZone.getTimeZone("GMT"));
        //DateFormat tf = DateFormat.getTimeInstance(DateFormat.LONG);
        //tf.setTimeZone(TimeZone.getTimeZone("GMT"));
        //String dateStr = df.format(timeSelected);
        //String timeStr = tf.format(timeSelected);
        //String dateStr = DateFormat.getDateInstance(DateFormat.LONG).format(timeSelected);
        //this.tvDurations[durationsIdx].setText(sdfDuration.format(timeSelected) + " : " + String.valueOf(durations[durationsIdx]) + " : "
        //    + "\n" + dateStr + " : " + timeStr);
        */
        long duration_in_ms = ((hourOfDay * 60) + minute) * 60 * 1000;
        this.durations[durationsIdx] = duration_in_ms;
        Date timeSelected = new Date(duration_in_ms);

        //this.tvDurations[durationsIdx].setText(sdfDuration.format(timeSelected) + " : " + String.valueOf(durations[durationsIdx]));
        this.tvDurations[durationsIdx].setText(sdfDuration.format(timeSelected));
    }

    public void onQuestionsDone(View view) {
        Intent intent = new Intent(this,EditDataActivity.class);
        intent.putExtra("caller","QuestionsActivity");
        intent.putExtra("date",date.getTime());
        intent.putExtra("time0",times[0].getTime());
        intent.putExtra("time1",times[1].getTime());
        intent.putExtra("time2",times[2].getTime());
        intent.putExtra("time3",times[3].getTime());
        intent.putExtra("duration0",durations[0]);
        intent.putExtra("duration1",durations[1]);
        intent.putExtra("duration2",durations[2]);
        intent.putExtra("naps",naps);
        intent.putExtra("quality",spnSleepQuality.getSelectedItemPosition()+1);
        startActivityForResult(intent,QUESTION_ACTIVITY_CODE);
    }

    public void onQuestionsBack(View view) {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QUESTION_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);
                this.finish();
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
        getMenuInflater().inflate(R.menu.questions, menu);
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
