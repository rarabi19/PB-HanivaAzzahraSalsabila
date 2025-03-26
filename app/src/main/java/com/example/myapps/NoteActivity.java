package com.example.myapps;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteActivity extends AppCompatActivity {

    private LinearLayout noteContainer;
    private FloatingActionButton addNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        // Inisialisasi UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noteContainer = findViewById(R.id.dynamicNoteContainer);
        addNoteButton = findViewById(R.id.addNote);

        // Tombol Navigasi
        ImageView btnUser = findViewById(R.id.btnuser);
        ImageView btnPengaturan = findViewById(R.id.btnpengaturan);
        ImageView btnHome = findViewById(R.id.btnhome);

        btnUser.setOnClickListener(v -> startActivity(new Intent(NoteActivity.this, UserActivity.class)));
        btnPengaturan.setOnClickListener(v -> startActivity(new Intent(NoteActivity.this, PengaturanActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(NoteActivity.this, HomeActivity.class)));

        // Event klik tombol tambah note
        addNoteButton.setOnClickListener(v -> showAddNoteDialog());
    }

    // Menampilkan Dialog Input Note
    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Catatan");

        final EditText input = new EditText(this);
        input.setHint("Tulis catatan...");
        builder.setView(input);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String noteText = input.getText().toString();
            if (!noteText.isEmpty()) {
                addNewNote(noteText);
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Menambahkan Note Secara Dinamis dengan Jarak 10dp
    private void addNewNote(String text) {
        LinearLayout noteLayout = new LinearLayout(this);
        noteLayout.setOrientation(LinearLayout.HORIZONTAL);
        noteLayout.setPadding(20, 20, 20, 20);
        noteLayout.setBackgroundResource(R.drawable.rounded_edittext);
        noteLayout.setGravity(Gravity.CENTER_VERTICAL);

        // Atur jarak antar note (marginTop 10dp)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 10, 0, 0); // Margin atas 10dp
        noteLayout.setLayoutParams(layoutParams);

        // TextView untuk isi note
        TextView noteTextView = new TextView(this);
        noteTextView.setText(text);
        noteTextView.setTextSize(16);
        noteTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        noteTextView.setPadding(10, 0, 10, 0);
        noteTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Tombol Hapus di sudut kanan bawah
        ImageView deleteButton = new ImageView(this);
        deleteButton.setImageResource(android.R.drawable.ic_delete);
        deleteButton.setPadding(10, 10, 10, 10);
        deleteButton.setOnClickListener(v -> noteContainer.removeView(noteLayout));

        // Menambahkan elemen ke layout
        noteLayout.addView(noteTextView);
        noteLayout.addView(deleteButton);
        noteContainer.addView(noteLayout);
    }
}