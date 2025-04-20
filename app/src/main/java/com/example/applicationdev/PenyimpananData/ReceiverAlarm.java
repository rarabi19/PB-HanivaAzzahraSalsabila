package com.example.applicationdev.PenyimpananData;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.applicationdev.Alarm;
import com.example.applicationdev.R;

public class ReceiverAlarm extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String label = intent.getStringExtra("label");
        boolean pengingat = intent.getBooleanExtra("pengingat", false);
        String nadaUri = intent.getStringExtra("nada");

        Uri soundUri = nadaUri != null && !nadaUri.isEmpty() ? Uri.parse(nadaUri) : RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, soundUri);
        ringtone.play();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        Intent i = new Intent(context, Alarm.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText(label != null ? label : "Bangun sekarang!")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, "Tunda", getSnoozePendingIntent(context, intent))
                .addAction(0, "OK", pendingIntent)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());

        if (pengingat) {
            // otomatis tunda setelah 2 menit jika tidak ditekan
            new Handler(Looper.getMainLooper()).postDelayed(() -> ringtone.play(), 2 * 60 * 1000);
        }
    }

    private PendingIntent getSnoozePendingIntent(Context context, Intent originalIntent) {
        Intent snoozeIntent = new Intent(context, ReceiverAlarm.class);
        snoozeIntent.putExtras(originalIntent);
        return PendingIntent.getBroadcast(context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
