package com.candidcold.cacophony;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.media.Ringtone;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.candidcold.cacophony.data.PhoneTone;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SelectedRingtonePreviewListener {

    public static final String TAG = MainActivity.class.getSimpleName();


    private ArrayList<PhoneTone> allRingtonesList;

    private boolean checked[];
    private String ringtoneNames[];
    private int counter = 0;

    private Ringtone playingRingtone;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    private ArrayList<PhoneTone> selectedRingtonesList;
    private Toolbar appBar;
    private TextView currentSong;

    private JobScheduler scheduler;
    private RecyclerView selectedRecyclerView;
    private SelectedTonesAdapter adapter;

    private RingtoneUtils ringtoneUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        ringtoneUtils = new RingtoneUtils(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        ringtoneUtils.stopPlayingRingtone();
    }

    @Override
    public void playTone(PhoneTone toneToPlay) {
        ringtoneUtils.playRingtone(toneToPlay);
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

    private void setupRecyclerView() {
        adapter = new SelectedTonesAdapter(selectedRingtonesList);
        selectedRecyclerView.setAdapter(adapter);
        selectedRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
}
