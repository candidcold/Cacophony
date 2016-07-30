package com.candidcold.cacophony;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by davidmorant on 3/22/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<com.candidcold.cacophony.PhoneTone> selectedTones;
        int numberOfTones,
            randomTonePosition;

        selectedTones = (ArrayList<PhoneTone>) intent.getSerializableExtra("SelectedTones");
        numberOfTones = selectedTones.size();
        randomTonePosition = getRandomNumber(numberOfTones);

        Uri newUri = Uri.parse(selectedTones.get(randomTonePosition).getTonePath());
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
        Log.d(RingtoneFragment.TAG, "Broadcast received, ringtone changed");

        // Build a notification to send to the user
        buildNotification(context, "New Ringtone!", selectedTones.get(randomTonePosition).getToneName());
    }

    private int getRandomNumber(int upperBound) {
        int randomNumber;
        Random rand = new Random();
        randomNumber = rand.nextInt(upperBound);

        return randomNumber;
    }

    private void buildNotification(Context context, String title, String body) {
        // Send a notification to the user that the ringtone has been changed
        int color = context.getResources().getColor(R.color.accent);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_queue_music_white_24dp)
                        .setColor(color)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

        // Handle when the notification is clicked to take it to the app's main activity.
        Intent resultIntent = new Intent(context, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
