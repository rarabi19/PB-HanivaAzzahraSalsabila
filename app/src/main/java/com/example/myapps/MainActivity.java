package com.example.myapps;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Deklarasi variabel
    private EditText namaEditText, nimEditText, loginEditText;
    private CheckBox checkBox;
    private Button signUpBtn;
    private TextView forgotPassword, registerNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Pastikan nama layout sesuai

        // Inisialisasi view berdasarkan ID
        namaEditText = findViewById(R.id.nama);
        nimEditText = findViewById(R.id.nim);
        loginEditText = findViewById(R.id.login);
        checkBox = findViewById(R.id.checkBox);
        signUpBtn = findViewById(R.id.signUpBtn);
        forgotPassword = findViewById(R.id.forgotPassword);
        registerNow = findViewById(R.id.registerNow);

        // Event klik tombol Sign Up
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = namaEditText.getText().toString().trim();
                String nim = nimEditText.getText().toString().trim();
                String password = loginEditText.getText().toString().trim();
                boolean isChecked = checkBox.isChecked();

                // Validasi input
                if (nama.isEmpty() || nim.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Harap isi semua data", Toast.LENGTH_SHORT).show();
                } else {
                    // Proses data (contoh: simpan, kirim ke server, dll)
                    String pesan = "Nama: " + nama + "\nNIM: " + nim + "\nPassword: " + password +
                            (isChecked ? "\nIngatkan: Ya" : "\nIngatkan: Tidak");

                    Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Event klik "Lupa Kata Sandi"
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Fitur Lupa Kata Sandi belum tersedia", Toast.LENGTH_SHORT).show();
            }
        });

        // Event klik "Daftar Sekarang"
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Menuju halaman pendaftaran...", Toast.LENGTH_SHORT).show();
                // Intent pindah halaman bisa ditambahkan di sini
            }
        });
    }
}
