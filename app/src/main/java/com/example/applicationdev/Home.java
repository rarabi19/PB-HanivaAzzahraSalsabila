package com.example.applicationdev;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Home extends AppCompatActivity {

    private Button bCatatan, bAlarm, bKalender, bList, bTabungan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        bCatatan = findViewById(R.id.bCatatan);
        bAlarm = findViewById(R.id.bAlarm);
        bKalender = findViewById(R.id.bKalender);
        bList = findViewById(R.id.bList);
        bTabungan = findViewById(R.id.bTabungan);

        ImageView bpengaturan = findViewById(R.id.bpengaturan);
        ImageView buser = findViewById(R.id.buser);
        ImageView bhome = findViewById(R.id.bhome);

        bpengaturan.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Pengaturan.class));
        });

        buser.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Profile.class));
        });

        bhome.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Home.class));
        });


        bCatatan.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Catatan.class));
        });

        bAlarm.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Alarm.class));
        });

        bKalender.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Kalender.class));
        });

        bList.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Kegiatan.class));
        });

        bTabungan.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Tabungan.class));
        });
    }
}