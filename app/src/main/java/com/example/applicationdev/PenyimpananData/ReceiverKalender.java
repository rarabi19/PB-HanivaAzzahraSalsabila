package com.example.applicationdev.PenyimpananData;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.applicationdev.R;

public class ReceiverKalender extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String label = intent.getStringExtra("label");
        String ringtone = intent.getStringExtra("ringtone");

        Uri soundUri = ringtone != null && !ringtone.isEmpty()
                ? Uri.parse(ringtone)
                : RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "reminder_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Pengingat", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(soundUri, null);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Pengingat Kalender")
                .setContentText(label)
                .setSound(soundUri)
                .setAutoCancel(true);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
