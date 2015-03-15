package com.android.candidcold.cacophony;

import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    Toolbar appBar;
    Button b;
    Button m;
    RingtoneManager rngtoneMngr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);
        b = (Button) findViewById(R.id.button_click);
        m = (Button) findViewById(R.id.another_button);

        // Get ringtones
        rngtoneMngr = new RingtoneManager(this);
        rngtoneMngr.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor alarmsCursor = rngtoneMngr.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            Log.d("com.android.candidcold", "There's a problem");
        }

        final Ringtone selectedRingtone = rngtoneMngr.getRingtone(this, rngtoneMngr.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE));

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRingtone.play();
            }
        });

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRingtone.stop();
            }
        });




    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
