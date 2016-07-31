package com.candidcold.cacophony;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.candidcold.cacophony.data.PhoneTone;

import java.util.Random;

public class RingtoneJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private int getRandomNumber(int upperBound) {
        return new Random().nextInt(upperBound);
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
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(mNotificationId, mBuilder.build());


        // Gets an instance of the NotificationManager service
//        NotificationManager mNotifyMgr =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    private void setDefaultRingtone(PhoneTone tone) {
        Uri newUri = Uri.parse(tone.getTonePath());
        RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri);
    }
}
