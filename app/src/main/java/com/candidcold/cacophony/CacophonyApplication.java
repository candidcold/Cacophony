package com.candidcold.cacophony;

import android.app.AlarmManager;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

public class CacophonyApplication extends Application {

    private JobScheduler scheduler;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createJobScheduler();
        /* TODO:
            Keep a singleton of the list of selected, and update it from various parts, possibly?
            That way, you can get the list from anywhere (namely the service)
         */

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
}
