package com.example.applicationdev;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText txnama, txEmail, txPassword, txPassword2;
    private Button bregister, blogin;
    private FirebaseAuth mAuth;

    // Dialog loading
    private AlertDialog loadingDialog;

    // Fungsi untuk setup dialog loading
    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.progres_layout, null);
        builder.setView(view);
        builder.setCancelable(false);  // Tidak bisa dibatalkan dari luar
        loadingDialog = builder.create();
    }

    // Tampilkan dialog
    private void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    // Sembunyikan dialog
    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txnama = findViewById(R.id.txnama);
        txEmail = findViewById(R.id.txEmail);
        txPassword = findViewById(R.id.txPassword);
        txPassword2 = findViewById(R.id.txPassword2);
        bregister = findViewById(R.id.bregister);
        blogin = findViewById(R.id.blogin);

        mAuth = FirebaseAuth.getInstance();
        setupLoadingDialog(); // Panggil setup dialog saat onCreate

        bregister.setOnClickListener(view -> {
            String nama = txnama.getText().toString().trim();
            String email = txEmail.getText().toString().trim();
            String password = txPassword.getText().toString().trim();
            String password2 = txPassword2.getText().toString().trim();

            if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
                Toast.makeText(this, " ⚠️ Pastikan tidak ada data yang kosong", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(password2)) {
                Toast.makeText(this, " ❌ Password tidak sama", Toast.LENGTH_SHORT).show();
            } else {
                showLoadingDialog(); // Tampilkan loading saat proses register
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            hideLoadingDialog(); // Sembunyikan loading setelah selesai
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(this, " ✅ Akun Berhasil di buat, Silahkan Login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Login.class));
                                finish();
                            } else {
                                Toast.makeText(this, " ❌ Gagal Register: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        blogin.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
        });
    }
}
