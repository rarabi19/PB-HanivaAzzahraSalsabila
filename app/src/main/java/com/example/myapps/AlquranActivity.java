package com.example.myapps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AlquranActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alquran);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi tombol
        ImageView btnuser = findViewById(R.id.btnuser);
        ImageView btnpengaturan = findViewById(R.id.btnpengaturan);
        ImageView btnhome = findViewById(R.id.btnhome);

        // Set event klik untuk setiap tombol
        btnuser.setOnClickListener(v -> startActivity(new Intent(AlquranActivity.this, UserActivity.class)));
        btnpengaturan.setOnClickListener(v -> startActivity(new Intent(AlquranActivity.this, PengaturanActivity.class)));
        btnhome.setOnClickListener(v -> startActivity(new Intent(AlquranActivity.this, HomeActivity.class)));
    }
}