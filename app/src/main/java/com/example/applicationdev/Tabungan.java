package com.example.applicationdev;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.core.content.res.ResourcesCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tabungan);

        Navbar.setup(this);

    }
}