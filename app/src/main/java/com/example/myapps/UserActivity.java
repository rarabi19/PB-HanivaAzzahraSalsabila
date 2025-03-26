package com.example.myapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imgProfile;
    private ImageView btnHome, btnSettings, btnUser;
    private Button btnLogout;
    private TextView tvUsername, tvEmail, tvNIM, tvTitle, tvHelo;
    private Uri profileImageUri;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inisialisasi Firebase Auth
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Pastikan pengguna telah login
        if (currentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Inisialisasi View
        imgProfile = findViewById(R.id.imgProfile);
        btnHome = findViewById(R.id.btnhome);
        btnSettings = findViewById(R.id.btnpengaturan);
        btnUser = findViewById(R.id.btnuser);
        btnLogout = findViewById(R.id.btnLogout);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvNIM = findViewById(R.id.tvNIM);
        tvTitle = findViewById(R.id.tvTitle);
        tvHelo = findViewById(R.id.tvhelo); // Pastikan sesuai dengan XML

        // Ambil data pengguna dari Firebase Database
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        // Tombol Navigasi
        btnHome.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, HomeActivity.class)));
        btnSettings.setOnClickListener(v -> openSettings());
        btnUser.setOnClickListener(v -> reloadUserData());  // Muat ulang data saat diklik
        imgProfile.setOnClickListener(this::showProfileMenu);
        btnLogout.setOnClickListener(view -> showLogoutDialog());

        // Muat Data dari Firebase
        loadUserProfile();
    }

    private void showProfileMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_upload_photo) {
                openGallery();
                return true;
            } else if (item.getItemId() == R.id.action_view_profile) {
                viewProfile();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            profileImageUri = data.getData();
            imgProfile.setImageURI(profileImageUri);
            saveProfileImage(profileImageUri);
            Toast.makeText(this, "Foto berhasil diunggah!", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewProfile() {
        if (profileImageUri != null) {
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("imageUri", profileImageUri.toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Tidak ada foto profil yang tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileImage(Uri imageUri) {
        userRef.child("profileImage").setValue(imageUri.toString());
    }

    private void loadUserProfile() {
        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                // Ambil data dari database
                String username = snapshot.child("username").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String nim = snapshot.child("nim").getValue(String.class);
                String imageUrl = snapshot.child("profileImage").getValue(String.class);

                // Set data ke TextView
                tvUsername.setText(username != null ? "Username : " + username : "Username : -");
                tvEmail.setText(email != null ? "Email : " + email : "Email : -");
                tvNIM.setText(nim != null ? "NIM : " + nim : "NIM : -");

                // Menampilkan sapaan
                tvHelo.setText(username != null ? "Hello, Selamat datang " + username + "...✨" : "Hello, Selamat datang...✨");

                // Tampilkan foto profil jika ada
                if (imageUrl != null) {
                    Uri imageUri = Uri.parse(imageUrl);  // Parse URL menjadi URI
                    imgProfile.setImageURI(imageUri);    // Set image URI langsung ke ImageView
                }

                // Set judul halaman
                tvTitle.setText("User Profile");
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Gagal memuat data!", Toast.LENGTH_SHORT).show());
    }

    private void reloadUserData() {
        Toast.makeText(this, "Memuat ulang data...", Toast.LENGTH_SHORT).show();
        loadUserProfile();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UserActivity.this, MainActivity.class));
                    finish();
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openSettings() {
        Toast.makeText(this, "Buka Pengaturan", Toast.LENGTH_SHORT).show();
    }
}
