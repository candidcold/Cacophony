package com.candidcold.cacophony;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.candidcold.cacophony.data.PhoneTone;

import java.util.ArrayList;

public class RingtoneUtils {
    private Context context;
    private Ringtone playingRingtone;
    private ArrayList<PhoneTone> allRingtones = new ArrayList<>();
    private String[] allRingtoneNames;
    private RingtoneManager ringtoneManager;

    public RingtoneUtils(Context context) {
        this.context = context;
        playingRingtone = getCurrentDefaultRingtone();
        playingRingtone.stop();
        ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);

    }

    public String[] getAllRingtonesOnDevice() {
        if (allRingtoneNames.length != 0) {
            return allRingtoneNames;

        } else {
            Cursor ringtonesCursor = ringtoneManager.getCursor();
            allRingtoneNames = new String[ringtonesCursor.getCount()];
            int counter = 0;

            while (ringtonesCursor.moveToNext()) {
                allRingtoneNames[counter] = ringtonesCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                counter++;

                PhoneTone temp = new PhoneTone();
                temp.setTonePath(ringtonesCursor.getString(RingtoneManager.URI_COLUMN_INDEX));
                temp.setToneName(ringtonesCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));

//                int currentPosition = ringtonesCursor.getPosition();
//                Uri currentUri = ringtoneManager.getRingtoneUri(currentPosition);
//                PhoneTone phoneTone = new PhoneTone();
//                phoneTone.setTonePath(currentUri.toString());
//                Ringtone tempRingtone = RingtoneManager.getRingtone(context, currentUri);
//                tempRingtone.stop();
//                phoneTone.setToneName(tempRingtone.getTitle(context));

                allRingtones.add(temp);

            }
            return allRingtoneNames;

        }
    }

    public Ringtone getCurrentDefaultRingtone() {
        Uri defaultRingtoneUri =
                RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        Ringtone current = RingtoneManager.getRingtone(context, defaultRingtoneUri);
        current.stop();
        return current;

    }

    public void playRingtone(PhoneTone tone) {
        stopPlayingRingtone();
        playingRingtone = RingtoneManager.getRingtone(context, Uri.parse(tone.getTonePath()));
        playingRingtone.play();

    }

    public void stopPlayingRingtone() {
        if (playingRingtone.isPlaying()) playingRingtone.stop();

    }

    public PhoneTone matchStringToRingtone(String name) {
        for (int i = 0; i < allRingtones.size(); i++) {
            if (name.equals(allRingtones.get(i).getToneName())) {
                return allRingtones.get(i);
            }
        }

        return new PhoneTone();
    }

    public boolean[] getCheckedItems(ArrayList<PhoneTone> alreadySelected) {
        boolean[] checkedList = new boolean[allRingtoneNames.length];

        for (PhoneTone tone : alreadySelected) {
            int position = ringtoneManager.getRingtonePosition(Uri.parse(tone.getTonePath()));
            if (position != -1) {
                checkedList[position] = true;
            }
        }

        return checkedList;
    }
}
