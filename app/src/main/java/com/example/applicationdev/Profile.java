package com.example.applicationdev;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applicationdev.PenyimpananData.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    private EditText txNama, txEmail, txNim, txJurusan, txUniversitas;
    private MaterialButton bEdit, bLogin;
    private ImageView bprofile;

    private boolean isEditing = false;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi view
        txNama = findViewById(R.id.txNama);
        txEmail = findViewById(R.id.txEmail);
        txNim = findViewById(R.id.txNim);
        txJurusan = findViewById(R.id.txJurusan);
        txUniversitas = findViewById(R.id.txUniversitas);

        bEdit = findViewById(R.id.bEdit);
        bLogin = findViewById(R.id.blogin);
        bprofile = findViewById(R.id.bprofile);

        Navbar.setup(this);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            currentEmail = user.getEmail();
            txEmail.setText(currentEmail);

            // Ambil data user dari Firebase saat halaman dibuka
            String userId = currentEmail.replace("@", "_").replace(".", "_");
            databaseReference.child(userId).get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    String nama = dataSnapshot.child("nama").getValue(String.class);
                    String nim = dataSnapshot.child("nim").getValue(String.class);
                    String jurusan = dataSnapshot.child("jurusan").getValue(String.class);
                    String universitas = dataSnapshot.child("universitas").getValue(String.class);

                    txNama.setText(nama);
                    txNim.setText(nim);
                    txJurusan.setText(jurusan);
                    txUniversitas.setText(universitas);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(Profile.this, "❌ Gagal memuat data profil.", Toast.LENGTH_SHORT).show();
            });

        } else {
            Toast.makeText(this, "❌ Anda belum login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Profile.this, Login.class));
            finish();
        }

        disableEditing();

        bEdit.setOnClickListener(view -> {
            if (!isEditing) {
                enableEditing();
                isEditing = true;
                bEdit.setText("Simpan");
            } else {
                saveProfileToDatabase();
                disableEditing();
                isEditing = false;
                bEdit.setText("Edit Profile");
            }
        });

        bLogin.setOnClickListener(view -> handleLoginLogout());

        bprofile.setOnClickListener(view -> showProfileOptions());
    }

    private void saveProfileToDatabase() {
        String nama = txNama.getText().toString().trim();
        String email = txEmail.getText().toString().trim();
        String nim = txNim.getText().toString().trim();
        String jurusan = txJurusan.getText().toString().trim();
        String universitas = txUniversitas.getText().toString().trim();

        if (nama.isEmpty() || email.isEmpty() || nim.isEmpty() || jurusan.isEmpty() || universitas.isEmpty()) {
            Toast.makeText(this, "❌ Semua data harus diisi.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = email.replace("@", "_").replace(".", "_");
        User profile = new User(nama, email, nim, jurusan, universitas);

        databaseReference.child(userId).setValue(profile)
                .addOnSuccessListener(unused -> Toast.makeText(Profile.this, "✅ Data berhasil disimpan.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Profile.this, "❌ Gagal menyimpan data.", Toast.LENGTH_SHORT).show());
    }

    private void handleLoginLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah Anda ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    mAuth.signOut();
                    startActivity(new Intent(Profile.this, Login.class));
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void showProfileOptions() {
        new AlertDialog.Builder(this)
                .setTitle("Profile")
                .setItems(new String[]{"Lihat Profile", "Ganti Profile"}, (dialog, which) -> {
                    if (which == 0) {
                        Toast.makeText(this, "👤 Nama: " + txNama.getText().toString(), Toast.LENGTH_SHORT).show();
                    } else if (which == 1) {
                        enableEditing();
                        isEditing = true;
                        bEdit.setText("Simpan");
                        Toast.makeText(this, "Data Berhasil Disimpan✨", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void disableEditing() {
        txNama.setEnabled(false);
        txEmail.setEnabled(false); // Email tetap tidak bisa diubah
        txNim.setEnabled(false);
        txJurusan.setEnabled(false);
        txUniversitas.setEnabled(false);
    }

    private void enableEditing() {
        txNama.setEnabled(true);
        txNim.setEnabled(true);
        txJurusan.setEnabled(true);
        txUniversitas.setEnabled(true);
    }
}
