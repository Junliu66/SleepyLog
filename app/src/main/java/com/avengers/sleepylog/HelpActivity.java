package com.avengers.sleepylog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void onHelpDone(View view) {
        this.finish();
    }
}
