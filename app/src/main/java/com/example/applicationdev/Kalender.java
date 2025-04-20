package com.example.applicationdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationdev.PenyimpananData.Pengingat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Kalender extends AppCompatActivity {

    private LinearLayout listViewKalender;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalender);

        listViewKalender = findViewById(R.id.listViewKalender);

        Navbar.setup(this);

        databaseRef = FirebaseDatabase.getInstance().getReference("pengingat");

        databaseRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listViewKalender.removeAllViews(); // hapus data lama

                for (DataSnapshot data : snapshot.getChildren()) {
                    Pengingat pengingat = data.getValue(Pengingat.class);

                    if (pengingat != null) {
                        // Inflate layout item_kalender.xml
                        View itemView = LayoutInflater.from(Kalender.this).inflate(R.layout.item_tanggal, listViewKalender, false);

                        TextView txTanggal = itemView.findViewById(R.id.txTanggal);
                        TextView txLabel = itemView.findViewById(R.id.txLabel);
                        TextView txDering = itemView.findViewById(R.id.txNada);
                        TextView txPengingat = itemView.findViewById(R.id.txPengingat);
                        TextView btnHapusDate = itemView.findViewById(R.id.btnHapusDate);

                        txTanggal.setText("Tanggal     : " + pengingat.getTanggal());
                        txLabel.setText("Label         : " + pengingat.getLabel());
                        txDering.setText("Nada          : " + pengingat.getDering());
                        txPengingat.setText("Ingatkan   : " + (pengingat.isAktif() ? "On" : "Off"));

                        btnHapusDate.setOnClickListener(v -> {
                            String id = data.getKey(); // Ambil ID unik dari snapshot
                            if (id != null) {
                                databaseRef.child(id).removeValue()
                                        .addOnSuccessListener(aVoid ->
                                                Toast.makeText(Kalender.this, "Tanggal berhasil dihapus", Toast.LENGTH_SHORT).show()
                                        )
                                        .addOnFailureListener(e ->
                                                Toast.makeText(Kalender.this, "Gagal menghapus pengingat", Toast.LENGTH_SHORT).show()
                                        );
                            }
                        });


                        // Tambahkan ke list
                        listViewKalender.addView(itemView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error handling
            }
        });

        ImageView btnTambah = findViewById(R.id.btntmbDate);
        btnTambah.setOnClickListener(v ->
                startActivity(new Intent(Kalender.this, SetelanKalender.class)));
    }
}
