package com.example.applicationdev;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationdev.PenyimpananData.Pengingat;
import com.example.applicationdev.PenyimpananData.ReceiverKalender;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetelanKalender extends AppCompatActivity {

    private TextView txLabel;
    private TextView txDering;
    private Switch txPengingat;
    private MaterialButton btnSetDate;
    private DatePicker tpDate;

    private Uri ringtoneUri;
    private Calendar calendar;

    private DatabaseReference databaseRef;

    private String labelKalender = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setelan_kalender);

        tpDate = findViewById(R.id.tpDate);
        txLabel = findViewById(R.id.txLabel);
        txDering = findViewById(R.id.txDering);
        txPengingat = findViewById(R.id.txPengingat);
        btnSetDate = findViewById(R.id.btnSetDate);

        ImageView bpengaturan = findViewById(R.id.bpengaturan);
        ImageView buser = findViewById(R.id.buser);
        ImageView bhome = findViewById(R.id.bhome);

        bpengaturan.setOnClickListener(view -> {
            startActivity(new Intent(SetelanKalender.this, Pengaturan.class));
        });

        buser.setOnClickListener(view -> {
            startActivity(new Intent(SetelanKalender.this, Profile.class));
        });

        bhome.setOnClickListener(view -> {
            startActivity(new Intent(SetelanKalender.this, Home.class));
        });


        calendar = Calendar.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("pengingat");

        // === Klik Label untuk input melalui dialog ===
        txLabel.setOnClickListener(v -> showLabelInputDialog());

        // === Pilih nada dering ===
        txDering.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Pilih Nada Dering");
            startActivityForResult(intent, 1);
        });

        // === Simpan Pengingat dan Arahkan ke Kalender.java ===
        btnSetDate.setOnClickListener(v -> {
            int day = tpDate.getDayOfMonth();
            int month = tpDate.getMonth();
            int year = tpDate.getYear();
            calendar.set(year, month, day);

            String id = databaseRef.push().getKey();
            String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
            String waktu = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
            String ringtone = ringtoneUri != null ? ringtoneUri.toString() : "";
            boolean aktif = txPengingat.isChecked();

            if (id == null) return;

            Pengingat pengingat = new Pengingat(id, labelKalender, waktu, tanggal, ringtone, aktif);
            databaseRef.child(id).setValue(pengingat);

            if (aktif) {
                setAlarm(id, labelKalender, ringtoneUri);
            }

            // Arahkan ke Kalender.java setelah simpan
            startActivity(new Intent(SetelanKalender.this, Kalender.class));
            finish();
        });
    }

    private void setAlarm(String id, String label, Uri ringtoneUri) {
        Intent intent = new Intent(this, ReceiverKalender.class);
        intent.putExtra("label", label);
        intent.putExtra("ringtone", ringtoneUri != null ? ringtoneUri.toString() : "");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void showLabelInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Masukkan Label Pengingat");

        final EditText input = new EditText(this);
        input.setHint("Contoh: Ulang Tahun, Acara Kampus");
        input.setText(labelKalender);

        builder.setView(input);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            labelKalender = input.getText().toString();
            txLabel.setText(labelKalender);
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            txDering.setText(ringtoneUri != null ? ringtoneUri.getLastPathSegment() : "Default");
        }
    }
}