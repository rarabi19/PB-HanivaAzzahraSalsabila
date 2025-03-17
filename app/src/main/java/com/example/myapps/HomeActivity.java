package com.example.myapps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi tombol
        Button quranBtn = findViewById(R.id.quranBtn);
        Button doaBtn = findViewById(R.id.doaBtn);
        Button asmaBtn = findViewById(R.id.asmaBtn);
        Button jadwalBtn = findViewById(R.id.jadwalBtn);
        Button kalenderBtn = findViewById(R.id.kalenderBtn);
        Button kiblatBtn = findViewById(R.id.kiblatBtn);

        // Set event klik untuk setiap tombol
        quranBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AlquranActivity.class)));
        doaBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, DoaActivity.class)));
        asmaBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AsmaActivity.class)));
        jadwalBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, JadwalActivity.class)));
        kalenderBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, KalenderActivity.class)));
        kiblatBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, KiblatActivity.class)));
    }
}
