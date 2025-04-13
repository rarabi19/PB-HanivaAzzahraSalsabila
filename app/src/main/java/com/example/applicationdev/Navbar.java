package com.example.applicationdev;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Navbar extends AppCompatActivity {

    private ImageView bpengaturan, buser, bhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);  // Memuat layout navbar

        // Menyambungkan ImageView dengan ID yang ada di layout
        bpengaturan = findViewById(R.id.bpengaturan);
        buser = findViewById(R.id.buser);
        bhome = findViewById(R.id.bhome);

        // Menangani klik untuk item Navbar
        bpengaturan.setOnClickListener(view -> {
            startActivity(new Intent(Navbar.this, Pengaturan.class));  // Menyambungkan ke halaman Pengaturan
        });

        buser.setOnClickListener(view -> {
            startActivity(new Intent(Navbar.this, Profile.class));  // Menyambungkan ke halaman Profil
        });

        bhome.setOnClickListener(view -> {
            startActivity(new Intent(Navbar.this, Home.class));  // Menyambungkan ke halaman Home
        });
    }
}
