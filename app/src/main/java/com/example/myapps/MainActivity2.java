package com.example.myapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.myapps.Models.UserDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    Button signUpBtn, loginBtn;
    TextInputEditText usernameSignUp, passwordSignUp, nimPengguna, emailPengguna;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private static final String TAG = "MainActivity2";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi View
        signUpBtn = findViewById(R.id.signUpBtn);
        loginBtn = findViewById(R.id.loginBtn);
        usernameSignUp = findViewById(R.id.usernameSignUp);
        emailPengguna = findViewById(R.id.emailPengguna);
        passwordSignUp = findViewById(R.id.passwordSingUp);
        nimPengguna = findViewById(R.id.nimPengguna);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Tombol Sign Up
        signUpBtn.setOnClickListener(view -> {
            String username = usernameSignUp.getText().toString().trim();
            String email = emailPengguna.getText().toString().trim();
            String password = passwordSignUp.getText().toString().trim();
            String NIM = nimPengguna.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                usernameSignUp.setError("Masukkan Username");
                usernameSignUp.requestFocus();
            } else if (TextUtils.isEmpty(email)) {
                emailPengguna.setError("Masukkan Email");
                emailPengguna.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                passwordSignUp.setError("Masukkan Password");
                passwordSignUp.requestFocus();
            } else if (password.length() < 6) {
                passwordSignUp.setError("Password minimal 6 karakter");
                passwordSignUp.requestFocus();
            } else if (TextUtils.isEmpty(NIM)) {
                nimPengguna.setError("Masukkan NIM");
                nimPengguna.requestFocus();
            } else if (!isNetworkAvailable()) {
                Toast.makeText(MainActivity2.this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
            } else {
                registerUser(username, email, password, NIM);
            }
        });

        // Tombol Login
        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, loginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser(String username, String email, String password, String NIM) {
        progressBar.setVisibility(View.VISIBLE);
        signUpBtn.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            signUpBtn.setEnabled(true);

            if (task.isSuccessful()) {
                FirebaseUser fUser = mAuth.getCurrentUser();

                if (fUser != null) {
                    String uid = fUser.getUid();
                    UserDetails userDetails = new UserDetails(uid, username, email, password, NIM);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(uid).setValue(userDetails).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(MainActivity2.this, "Akun berhasil dibuat! Selamat datang!", Toast.LENGTH_LONG).show();

                            // Langsung arahkan ke HomeActivity
                            Intent intent = new Intent(MainActivity2.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "Gagal menyimpan data ke database: ", task1.getException());
                            Toast.makeText(MainActivity2.this, "Gagal menyimpan data, coba lagi.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e(TAG, "FirebaseUser null setelah pendaftaran!");
                    Toast.makeText(MainActivity2.this, "Pendaftaran gagal, coba lagi.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Exception exception = task.getException();
                if (exception instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(MainActivity2.this, "Email sudah terdaftar!", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "Pendaftaran gagal: ", exception);
                    Toast.makeText(MainActivity2.this, "Pendaftaran gagal: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        boolean isConnected = capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        return isConnected;
    }
}
