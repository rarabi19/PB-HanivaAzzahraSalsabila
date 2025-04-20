package com.example.applicationdev;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText txEmail, txPassword;
    private Button blogin, bregister;
    private FirebaseAuth mAuth;

    // Loading dialog instance
    private AlertDialog loadingDialog;

    // Setup only once
    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.progres_layout, null);
        builder.setView(view);
        builder.setCancelable(false); // Prevent dismiss on outside touch
        loadingDialog = builder.create();
    }

    private void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txEmail = findViewById(R.id.txEmail);
        txPassword = findViewById(R.id.txPassword);
        blogin = findViewById(R.id.blogin);
        bregister = findViewById(R.id.bregister);


        mAuth = FirebaseAuth.getInstance();
        setupLoadingDialog();

        bregister.setOnClickListener(view -> {
            startActivity(new Intent(Login.this, Register.class));
        });

        blogin.setOnClickListener(view -> {
            String email = txEmail.getText().toString().trim();
            String password = txPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, " ❌ Email dan Password tidak boleh kosong ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, " ❌ Format email tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            showLoadingDialog(); // Menampilkan loading saat login

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        hideLoadingDialog(); // Sembunyikan loading setelah proses

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(this, " ✅ Login Berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Profile.class);
                            intent.putExtra("email", user.getEmail());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, " ❌ Login gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
