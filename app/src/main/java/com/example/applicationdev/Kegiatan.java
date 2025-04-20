package com.example.applicationdev;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import androidx.core.content.res.ResourcesCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationdev.PenyimpananData.Task;
import com.google.firebase.database.*;

import java.util.*;

public class Kegiatan extends AppCompatActivity {

    private LinearLayout taskBelumContainer, taskSelesaiContainer;
    private ImageView btntbhkegiatan;
    private DatabaseReference dbRef;

    public List<Task> semuaKegiatan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan);

        taskBelumContainer = findViewById(R.id.taskBelumContainer);
        taskSelesaiContainer = findViewById(R.id.taskSelesaiContainer);
        btntbhkegiatan = findViewById(R.id.btntbhkegiatan);

        Navbar.setup(this);

        dbRef = FirebaseDatabase.getInstance().getReference("kegiatan");

        btntbhkegiatan.setOnClickListener(v -> tampilkanInputDialog(null));

        muatKegiatanDariFirebase();
    }

    private void tampilkanInputDialog(Task task) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        EditText input = new EditText(this);
        input.setHint("📝 Tulis kegiatan...");
        if (task != null) input.setText(task.title);
        layout.addView(input);

        Spinner spinner = new Spinner(this);
        String[] priorities = {"🔵  Penting", "🔴  Mendesak", "🟣  Bisa Nanti"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorities);
        spinner.setAdapter(adapter);
        if (task != null) spinner.setSelection(adapter.getPosition(task.priority));
        layout.addView(spinner);

        new AlertDialog.Builder(this)
                .setTitle(task == null ? "Tambah Kegiatan" : "Edit Kegiatan")
                .setView(layout)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    String title = input.getText().toString().trim();
                    String priority = spinner.getSelectedItem().toString();

                    if (!title.isEmpty()) {
                        if (task == null) {
                            // Tambah baru
                            String id = dbRef.push().getKey();
                            Map<String, Object> data = new HashMap<>();
                            data.put("title", title);
                            data.put("done", false);
                            data.put("priority", priority);
                            dbRef.child(id).setValue(data);
                        } else {
                            // Edit
                            Map<String, Object> update = new HashMap<>();
                            update.put("title", title);
                            update.put("priority", priority);
                            dbRef.child(task.id).updateChildren(update);
                        }
                    } else {
                        Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void muatKegiatanDariFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                semuaKegiatan.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String id = item.getKey();
                    String title = item.child("title").getValue(String.class);
                    Boolean done = item.child("done").getValue(Boolean.class);
                    String priority = item.child("priority").getValue(String.class);

                    if (id != null && title != null && done != null && priority != null) {
                        semuaKegiatan.add(new Task(id, title, done, priority));
                    }
                }
                tampilkanSemuaKegiatan();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Kegiatan.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tampilkanSemuaKegiatan() {
        taskBelumContainer.removeAllViews();
        taskSelesaiContainer.removeAllViews();

        for (Task task : semuaKegiatan) {
            if (!task.done) {
                tambahKegiatanKeTampilan(taskBelumContainer, task);
            }
        }

        for (Task task : semuaKegiatan) {
            if (task.done) {
                tambahKegiatanKeTampilan(taskSelesaiContainer, task);
            }
        }
    }

    private void tambahKegiatanKeTampilan(LinearLayout container, Task task) {
        LinearLayout baris = new LinearLayout(this);
        baris.setOrientation(LinearLayout.HORIZONTAL);
        baris.setPadding(20, 10, 20, 10);
        baris.setGravity(Gravity.CENTER_VERTICAL);
        baris.setBackgroundResource(R.drawable.edit_text3);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setText("[" + task.priority + "] " + task.title);
        checkBox.setChecked(task.done);
        checkBox.setTextSize(16);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbRef.child(task.id).child("done").setValue(isChecked);
        });

        TextView editBtn = new TextView(this);
        editBtn.setText("✏️");
        editBtn.setTextSize(20);
        editBtn.setPadding(20, 0, 20, 0);
        editBtn.setOnClickListener(v -> tampilkanInputDialog(task));

        TextView deleteBtn = new TextView(this);
        deleteBtn.setText("❌");
        deleteBtn.setTextSize(22);
        deleteBtn.setPadding(10, 0, 20, 0);
        deleteBtn.setOnClickListener(v -> dbRef.child(task.id).removeValue());

        baris.addView(checkBox);
        baris.addView(editBtn);
        baris.addView(deleteBtn);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        container.addView(baris, params);
    }
}
