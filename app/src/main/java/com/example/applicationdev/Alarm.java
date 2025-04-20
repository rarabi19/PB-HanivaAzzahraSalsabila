package com.example.applicationdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Alarm extends AppCompatActivity {

    private LinearLayout notesContainer;
    private ImageView btntbhAlarm;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm);

        Navbar.setup(this);

        notesContainer = findViewById(R.id.notesContainer);
        btntbhAlarm = findViewById(R.id.btntbhAlarm);
        dbRef = FirebaseDatabase.getInstance().getReference("Alarm");

        loadAlarms();
        btntbhAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(Alarm.this, SetelAlarm.class);
            startActivity(intent);
        });

    }
    private void loadAlarms() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                notesContainer.removeAllViews();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String id = data.getKey();
                    int jam = data.child("jam").getValue(Integer.class) != null ? data.child("jam").getValue(Integer.class) : 0;
                    int menit = data.child("menit").getValue(Integer.class) != null ? data.child("menit").getValue(Integer.class) : 0;
                    String label = String.valueOf(data.child("label").getValue());
                    String nada = String.valueOf(data.child("nada").getValue());
                    boolean pengingat = Boolean.TRUE.equals(data.child("pengingat").getValue(Boolean.class));

                    View cardView = LayoutInflater.from(Alarm.this).inflate(R.layout.item_alarm, notesContainer, false);

                    TextView txWaktu = cardView.findViewById(R.id.txWaktu);
                    TextView txLabel = cardView.findViewById(R.id.txLabel);
                    TextView txNada = cardView.findViewById(R.id.txNada);
                    TextView txPengingat = cardView.findViewById(R.id.txPengingat);
                    TextView btnHapus = cardView.findViewById(R.id.btnHapusAlarm);

                    txWaktu.setText("🕒 Pukul        : " + String.format("%02d", jam) + ":" + String.format("%02d", menit));
                    txLabel.setText("📝 Label        : " + (label == null || label.isEmpty() ? "Tanpa Label" : label));
                    txNada.setText("🎵 Nada         : " + (nada == null || nada.isEmpty() ? "Default" : nada));
                    txPengingat.setText("🔁 Pengingat  : " + (pengingat ? "Aktif" : "Nonaktif"));

                    btnHapus.setOnClickListener(v -> {
                        if (id != null) {
                            dbRef.child(id).removeValue().addOnSuccessListener(aVoid ->
                                    Toast.makeText(Alarm.this, "Alarm berhasil dihapus", Toast.LENGTH_SHORT).show()
                            ).addOnFailureListener(e ->
                                    Toast.makeText(Alarm.this, "Gagal menghapus alarm", Toast.LENGTH_SHORT).show()
                            );
                        }
                    });

                    notesContainer.addView(cardView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Alarm.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
