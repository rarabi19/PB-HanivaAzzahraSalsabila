package com.example.myapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private ProgressDialog progressDialog;

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

        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);

        // Cek apakah pengguna sudah login sebelumnya
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            goToMainActivity();
        }

        // Tombol Login
        loginBtn.setOnClickListener(view -> loginUser());

        // Pindah ke halaman Sign Up
        toSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(loginActivity.this, MainActivity2.class);
            startActivity(intent);
        });
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

        progressDialog.show(); // Tampilkan loading dialog

        // Proses login dengan Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss(); // Sembunyikan loading setelah selesai
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(loginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                            goToMainActivity();
                        } else {
                            Toast.makeText(loginActivity.this, "Silakan verifikasi email Anda sebelum login!", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        Toast.makeText(loginActivity.this, "Login gagal! Periksa kembali email dan password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(loginActivity.this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
