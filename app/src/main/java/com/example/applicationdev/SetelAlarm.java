package com.example.applicationdev;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationdev.PenyimpananData.ReceiverAlarm;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SetelAlarm extends AppCompatActivity {

    private TextView txLabel, txDering;
    private Switch txPengingat;
    private MaterialButton bAlarm;
    private TimePicker tpAlarm;

    private int jam, menit;
    private String label = "";
    private String nadaDering = "";
    private boolean pengingat = false;

    private Uri selectedRingtoneUri;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setelan_alarm);

        txLabel = findViewById(R.id.txLabel);
        txDering = findViewById(R.id.txDering);
        txPengingat = findViewById(R.id.txPengingat);
        bAlarm = findViewById(R.id.bAlarm);
        tpAlarm = findViewById(R.id.tpAlarm);

        Navbar.setup(this);

        dbRef = FirebaseDatabase.getInstance().getReference("Alarm");

        if (Build.VERSION.SDK_INT >= 23) {
            jam = tpAlarm.getHour();
            menit = tpAlarm.getMinute();
        } else {
            jam = tpAlarm.getCurrentHour();
            menit = tpAlarm.getCurrentMinute();
        }

        tpAlarm.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            jam = hourOfDay;
            menit = minute;
        });

        txLabel.setOnClickListener(v -> showLabelDialog());

        txDering.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Pilih Nada Dering");
            startActivityForResult(intent, 1);
        });

        bAlarm.setOnClickListener(v -> {
            pengingat = txPengingat.isChecked();
            setTimer();
            notification();
            saveToDatabase();
            Toast.makeText(SetelAlarm.this, "Alarm disetel pukul " + jam + ":" + menit, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SetelAlarm.this, Alarm.class);
            startActivity(intent);
        });
    }

    private void showLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Masukkan Label Alarm");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            label = input.getText().toString();
            if (label.isEmpty()) {
                label = "Alarm tanpa label";
            }
            txLabel.setText("     Label: " + label);
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Remainder";
            String description = "Hey Wake Up!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Notify", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void setTimer() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Date date = new Date();

        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY, jam);
        cal_alarm.set(Calendar.MINUTE, menit);
        cal_alarm.set(Calendar.SECOND, 0);

        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
            Toast.makeText(this, "Jam yang dipilih sudah lewat, alarm akan disetel untuk besok.", Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(SetelAlarm.this, ReceiverAlarm.class);
        i.putExtra("label", label);
        i.putExtra("pengingat", pengingat);
        i.putExtra("nada", selectedRingtoneUri != null ? selectedRingtoneUri.toString() : "");

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, flags);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
        }
    }

    private void saveToDatabase() {
        String id = dbRef.push().getKey();
        Map<String, Object> data = new HashMap<>();
        data.put("jam", jam);
        data.put("menit", menit);
        data.put("label", label);
        data.put("nada", selectedRingtoneUri != null ? selectedRingtoneUri.toString() : "");
        data.put("pengingat", pengingat);

        if (id != null) {
            dbRef.child(id).setValue(data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedRingtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (selectedRingtoneUri != null) {
                nadaDering = RingtoneManager.getRingtone(this, selectedRingtoneUri).getTitle(this);
                txDering.setText("     Nada Dering: " + nadaDering);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
