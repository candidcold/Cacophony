package com.android.candidcold.cacophony;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by davidmorant on 3/14/15.
 *
 */
public class RingtoneFragment extends Fragment {
    public static final String TAG = MainActivity.class.getSimpleName();

    private TextView currentSong;
    private ListView listView;
    private Cursor ringtonesCursor;
    private RingtoneManager ringtoneManager;
    private Context fragmentContext;
    private RingtoneAdapter adapter;
    private ArrayList<PhoneTone> ringtoneList;
    private Ringtone playingRingtone;
    private FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // For ease.
        fragmentContext = getActivity().getApplicationContext();

        ringtoneManager = new RingtoneManager(fragmentContext);
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
        ringtonesCursor = ringtoneManager.getCursor();
        ringtoneList = new ArrayList<>();
        Log.e(TAG, "Hello " + ringtonesCursor.getCount());

        for (ringtonesCursor.moveToFirst(); !ringtonesCursor.isAfterLast(); ringtonesCursor.moveToNext()) {
            // Get the information of ringtone
            int currentPosition = ringtonesCursor.getPosition();
            Uri currentUri = ringtoneManager.getRingtoneUri(currentPosition);
            PhoneTone phoneTone = new PhoneTone();
            phoneTone.setTonePath(currentUri);
            phoneTone.setToneName(ringtoneManager.getRingtone(fragmentContext, currentUri).getTitle(fragmentContext));
//            Log.d(TAG, "David  " + phoneTone.getToneName());

            // Add the phoneTone to the list
            ringtoneList.add(phoneTone);
        }
        adapter = new RingtoneAdapter(fragmentContext, ringtoneList);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "The fragment was created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ringtones, container, false);
        currentSong = (TextView) rootView.findViewById(R.id.current_song);
        // Get the current song
        currentSong.setText(getCurrentDefaultRingtone().getTitle(fragmentContext));
        playingRingtone = getCurrentDefaultRingtone();

        listView = (ListView) rootView.findViewById(R.id.list_view);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Stop the ringtone playing before this one to prevent overlap
                playingRingtone.stop();
                playingRingtone = ringtoneManager.getRingtone(fragmentContext, ringtoneList.get(position).getTonePath());
                playingRingtone.play();
            }
        });


        return rootView;
    }

    // Retrieve the current ringtone
    private Ringtone getCurrentDefaultRingtone() {
        Uri defaultRingtoneUri = ringtoneManager.getActualDefaultRingtoneUri(fragmentContext, RingtoneManager.TYPE_RINGTONE);
        Ringtone currentTone = ringtoneManager.getRingtone(fragmentContext, defaultRingtoneUri);
        return currentTone;
    }
}
