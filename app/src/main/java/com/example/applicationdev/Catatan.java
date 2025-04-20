package com.example.applicationdev;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.util.*;

public class Catatan extends AppCompatActivity {

    private EditText cariFile;
    private ImageView btnTmbkegiatan;
    private LinearLayout notesContainer;
    private List<String> allNotes = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catatan);

        cariFile = findViewById(R.id.cariFile);
        btnTmbkegiatan = findViewById(R.id.btmbkegiatan);
        notesContainer = findViewById(R.id.notesContainer);

        // Style pencarian
        cariFile.setTextColor(Color.BLACK);
        cariFile.setHintTextColor(Color.GRAY);

        dbRef = FirebaseDatabase.getInstance().getReference("notes");

        loadNotesFromFirebase();

        // Navigasi
        Navbar.setup(this);

        btnTmbkegiatan.setOnClickListener(v -> showInputDialog());

        cariFile.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshNotes( s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void showInputDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_input_catatan, null);
        EditText input = dialogView.findViewById(R.id.editTextNote);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Tambah Catatan")
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog1, which) -> {
                    String note = input.getText().toString().trim();
                    if (!note.isEmpty()) {
                        String id = dbRef.push().getKey();
                        dbRef.child(id).setValue(note);
                    }
                })
                .setNegativeButton("Batal", null)
                .create();

        dialog.show();
    }

    private void showEditDialog(String id, String oldNote) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_input_catatan, null);
        EditText input = dialogView.findViewById(R.id.editTextNote);
        input.setText(oldNote);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Catatan")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog1, which) -> {
                    String updatedNote = input.getText().toString().trim();
                    if (!updatedNote.isEmpty()) {
                        dbRef.child(id).setValue(updatedNote);
                    }
                })
                .setNegativeButton("Batal", null)
                .create();

        dialog.show();
    }

    private void loadNotesFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allNotes.clear();
                for (DataSnapshot noteSnap : snapshot.getChildren()) {
                    allNotes.add(noteSnap.getKey() + ":" + noteSnap.getValue(String.class));
                }
                refreshNotes(cariFile.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Catatan.this, "Gagal memuat data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshNotes(String keyword) {
        notesContainer.removeAllViews();

        for (String item : allNotes) {
            String[] parts = item.split(":", 2);
            String id = parts[0];
            String note = parts[1];

            if (note.toLowerCase().contains(keyword.toLowerCase())) {
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(20, 20, 20, 20);
                itemLayout.setBackgroundResource(R.drawable.edit_text3);
                itemLayout.setGravity(Gravity.CENTER_VERTICAL);

                TextView textView = new TextView(this);
                textView.setText(note);
                textView.setTextSize(18);
                textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                textView.setTextColor(Color.BLACK);

                textView.setOnClickListener(v -> showEditDialog(id, note));

                TextView deleteBtn = new TextView(this);
                deleteBtn.setText("❌");
                deleteBtn.setTextSize(18);
                deleteBtn.setOnClickListener(v -> dbRef.child(id).removeValue());

                itemLayout.addView(textView);
                itemLayout.addView(deleteBtn);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(25, 15, 22, 10);
                notesContainer.addView(itemLayout, params);
            }
        }
    }
}
