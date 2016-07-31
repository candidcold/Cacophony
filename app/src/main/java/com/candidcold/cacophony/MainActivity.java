package com.candidcold.cacophony;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.candidcold.cacophony.data.PhoneTone;

import java.util.ArrayList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements SelectedRingtonePreviewListener {

    private Toolbar appBar;
    public static final String TAG = MainActivity.class.getSimpleName();

    private TextView currentSong;
    private Cursor ringtonesCursor;

    private ArrayList<PhoneTone> allRingtonesList;
    private ArrayList<PhoneTone> selectedRingtonesList;
    private Queue<PhoneTone> tonesToUpdate;


    private boolean checked[];
    private String ringtoneNames[];
    private int counter = 0;

    private Ringtone playingRingtone;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    private JobScheduler scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

//        // Setup fragment transaction
//        if (savedInstanceState == null) {
//            RingtoneFragment ringtoneFragment = new com.candidcold.cacophony.RingtoneFragment();
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.add(R.id.container, ringtoneFragment);
//            ft.commit();
//        }

    }

    @Override
    public void playTone(PhoneTone toneToPlay) {

    }

    // Will probably need to move this into the onclick in the fragment or something
    private void createJobScheduler() {
        scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder =
                new JobInfo.Builder(1, new ComponentName(getPackageName(), RingtoneJobService.class.getName()));

        builder.setPersisted(true);
        builder.setPeriodic(AlarmManager.INTERVAL_DAY);
        // TODO: Add more as needed

        scheduler.schedule(builder.build());
    }

    private void cancelJobScheduler() {
        scheduler.cancelAll();
    }

    private void snackbarMaker(View v, String text) {
        Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
    }
}
