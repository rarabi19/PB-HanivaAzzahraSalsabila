package com.example.myapps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class PengaturanActivity extends AppCompatActivity {

    private Switch switchTheme, switchNotifications;
    private Spinner spinnerLanguage;
    private SeekBar seekbarFontSize;
    private MaterialButton btnClearCache;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        // Inisialisasi komponen UI
        switchTheme = findViewById(R.id.tema);
        switchNotifications = findViewById(R.id.switchnotifications);
        spinnerLanguage = findViewById(R.id.language);
        seekbarFontSize = findViewById(R.id.barfontSize);
        btnClearCache = findViewById(R.id.clearcache);

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

        // Load pengaturan yang tersimpan
        loadSettings();

        // Event Listener untuk switch tema
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            Toast.makeText(this, "Tema diperbarui", Toast.LENGTH_SHORT).show();
        });

        // Event Listener untuk switch notifikasi
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notifications", isChecked);
            editor.apply();
            Toast.makeText(this, "Notifikasi diperbarui", Toast.LENGTH_SHORT).show();
        });

        // Event Listener untuk ukuran font
        seekbarFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("font_size", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(PengaturanActivity.this, "Ukuran font diperbarui", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol hapus cache
        btnClearCache.setOnClickListener(v -> {
            clearCache();
            Toast.makeText(PengaturanActivity.this, "Cache berhasil dihapus", Toast.LENGTH_SHORT).show();
        });

        // Inisialisasi Navbar dan Event Click
        setupNavbar();
    }

    private void setupNavbar() {
        View navbar = findViewById(R.id.navbar);
        if (navbar != null) {
            ImageView btnHome = navbar.findViewById(R.id.btnhome);
            ImageView btnUser = navbar.findViewById(R.id.btnuser);
            ImageView btnPengaturan = navbar.findViewById(R.id.btnpengaturan);

            if (btnHome != null) {
                btnHome.setOnClickListener(v -> startActivity(new Intent(PengaturanActivity.this, HomeActivity.class)));
            }

            if (btnUser != null) {
                btnUser.setOnClickListener(v -> startActivity(new Intent(PengaturanActivity.this, UserActivity.class)));
            }

            if (btnPengaturan != null) {
                btnPengaturan.setOnClickListener(v -> startActivity(new Intent(PengaturanActivity.this, PengaturanActivity.class)));
            }
        }
    }

    private void loadSettings() {
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        int fontSize = sharedPreferences.getInt("font_size", 16);

        switchTheme.setChecked(isDarkMode);
        switchNotifications.setChecked(isNotificationsEnabled);
        seekbarFontSize.setProgress(fontSize);
    }

    private void clearCache() {
        File cacheDir = getCacheDir();
        if (cacheDir != null && cacheDir.isDirectory()) {
            for (File file : cacheDir.listFiles()) {
                file.delete();
            }
        }
    }
}
