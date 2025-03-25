package com.example.myapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapps.Models.ViewProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imgProfile, btnHome;
    private Button btnLogout;
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        imgProfile = findViewById(R.id.imgProfile);
        btnHome = findViewById(R.id.btnHome);
        btnLogout = findViewById(R.id.btnLogout);

        btnHome.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, HomeActivity.class)));
        imgProfile.setOnClickListener(this::showProfileMenu);
        btnLogout.setOnClickListener(view -> showLogoutDialog());

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
            Intent intent = new Intent(this, ViewProfileActivity.class);
            intent.putExtra("imageUri", profileImageUri.toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Tidak ada foto profil yang tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileImage(Uri imageUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            reference.child("profileImage").setValue(imageUri.toString());
        }
    }

    private void loadUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            reference.child("profileImage").get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    profileImageUri = Uri.parse(snapshot.getValue(String.class));
                    imgProfile.setImageURI(profileImageUri);
                }
            });
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UserActivity.this, MainActivity2.class));
                    finish();
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }
}