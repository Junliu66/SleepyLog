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
import java.util.Date;

public class QuestionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int QUESTION_ACTIVITY_CODE = 2;
    Date date;
    TextView tvDate;
    Date[] times;
    TextView[] tvTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
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
        tvDate = (TextView) findViewById(R.id.tvQDate);
        TextView tvTime0 = (TextView) findViewById(R.id.tvQTime0);
        TextView tvTime1 = (TextView) findViewById(R.id.tvQTime1);
        TextView tvTime2 = (TextView) findViewById(R.id.tvQTime2);
        TextView tvTime3 = (TextView) findViewById(R.id.tvQTime3);
        tvTimes = new TextView[] {tvTime0,tvTime1,tvTime2,tvTime3};
        times = new Date[4];

        Bundle extras = getIntent().getExtras();
        date = new Date(extras.getLong("date"));
        tvDate.setText(DateFormat.getDateInstance().format(date));
        for (int i=0; i<4; i++) {
            times[i] = new Date(extras.getLong("time"+i));
            tvTimes[i].setText(DateFormat.getTimeInstance().format(times[i]));
        }
    }

    public void onQuestionsDone(View view) {
        Intent editDataIntent = new Intent(this,EditDataActivity.class);
        // questionsIntent.putExtras(...);
        startActivityForResult(editDataIntent,QUESTION_ACTIVITY_CODE);
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
