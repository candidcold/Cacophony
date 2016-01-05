package com.candidcold.cacophony;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends ActionBarActivity {

    Toolbar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        // Setup fragment transaction
        if (savedInstanceState == null) {
            com.candidcold.cacophony.RingtoneFragment ringtoneFragment = new com.candidcold.cacophony.RingtoneFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, ringtoneFragment);
            ft.commit();
        }

    }

}
