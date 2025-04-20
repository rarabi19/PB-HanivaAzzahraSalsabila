package com.example.applicationdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Home extends AppCompatActivity {

    private CardView cardTodo, cardNotes, cardAlarm;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Inisialisasi CardView
        cardTodo = findViewById(R.id.card_todo);
        cardNotes = findViewById(R.id.card_notes);
        cardAlarm = findViewById(R.id.card_alarm);
        calendarView = findViewById(R.id.calendarView);

        // Navbar
        Navbar.setup(this);

        // Klik Card ToDo -> Kegiatan.java
        cardTodo.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Kegiatan.class));
        });

        // Klik Card Catatan -> Catatan.java
        cardNotes.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Catatan.class));
        });

        // Klik Kalender -> Kalender.java
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            startActivity(new Intent(Home.this, Kalender.class));
        });

        // Klik Card Alarm -> Alarm.java
        cardAlarm.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Alarm.class));
        });

    }
}
