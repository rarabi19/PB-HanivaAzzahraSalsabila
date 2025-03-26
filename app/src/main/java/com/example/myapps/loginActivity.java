package com.example.myapps;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    private TextInputEditText emailLogin, passwordLogin;
    private Button loginBtn;
    private TextView toSignUp;
    private FirebaseAuth mAuth;
    private AlertDialog loadingDialog;  // Menggunakan AlertDialog untuk loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi FirebaseAuth dan UI
        mAuth = FirebaseAuth.getInstance();
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginBtn = findViewById(R.id.loginBtn);
        toSignUp = findViewById(R.id.toSignUp);

        // Inisialisasi Dialog Loading
        setupLoadingDialog();

        // Cek apakah pengguna sudah login sebelumnya
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToHomeActivity(); // Langsung ke HomeActivity jika sudah login sebelumnya
        }

        // Tombol Login
        loginBtn.setOnClickListener(view -> loginUser());

        // Pindah ke halaman Sign Up
        toSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(loginActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }

    // Fungsi untuk menyiapkan dialog loading
    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.progress_layout, null);
        builder.setView(view);
        builder.setCancelable(false);  // Agar tidak bisa ditutup dengan klik luar
        loadingDialog = builder.create();
    }

    // Fungsi untuk menampilkan dialog loading
    private void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    // Fungsi untuk menyembunyikan dialog loading
    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void loginUser() {
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(email)) {
            emailLogin.setError("Email tidak boleh kosong!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordLogin.setError("Password tidak boleh kosong!");
            return;
        }

        showLoadingDialog(); // Tampilkan loading dialog

        // Proses login dengan Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideLoadingDialog(); // Sembunyikan loading setelah selesai
                    if (task.isSuccessful()) {
                        Toast.makeText(loginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                        goToHomeActivity();
                    } else {
                        Toast.makeText(loginActivity.this, "Login gagal! Periksa kembali email dan password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(loginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
