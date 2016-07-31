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

    public RingtoneUtils(Context context) {
        this.context = context;
        playingRingtone = getCurrentDefaultRingtone();
        playingRingtone.stop();

    }

    public ArrayList<PhoneTone> getAllRingtonesOnDevice() {
        if (!allRingtones.isEmpty()) {
            return allRingtones;

        } else {
            RingtoneManager ringtoneManager = new RingtoneManager(context);
            ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);
            Cursor ringtonesCursor = ringtoneManager.getCursor();

            while (ringtonesCursor.moveToNext()) {
                int currentPosition = ringtonesCursor.getPosition();
                Uri currentUri = ringtoneManager.getRingtoneUri(currentPosition);
                PhoneTone phoneTone = new PhoneTone();
                phoneTone.setTonePath(currentUri.toString());
                Ringtone tempRingtone = RingtoneManager.getRingtone(context, currentUri);
                tempRingtone.stop();
                phoneTone.setToneName(tempRingtone.getTitle(context));

                allRingtones.add(phoneTone);

            }
            return allRingtones;

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
}
