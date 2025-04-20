package com.example.applicationdev;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class Navbar {

    public static void setup(final Activity activity) {
        ImageView bpengaturan = activity.findViewById(R.id.bpengaturan);
        ImageView buser = activity.findViewById(R.id.buser);
        ImageView bhome = activity.findViewById(R.id.bhome);

        if (bpengaturan != null) {
            bpengaturan.setOnClickListener(view ->
                    activity.startActivity(new Intent(activity, Pengaturan.class))
            );
        }

        if (buser != null) {
            buser.setOnClickListener(view ->
                    activity.startActivity(new Intent(activity, Profile.class))
            );
        }

        if (bhome != null) {
            bhome.setOnClickListener(view ->
                    activity.startActivity(new Intent(activity, Home.class))
            );
        }
    }
}
