package com.example.myapps;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapps.Models.UserDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

public class MainActivity2 extends AppCompatActivity {

    Button signUpBtn;
    TextInputEditText usernameSignUp, passwordSingUp, nimPengguna, emailPengguna;
    FirebaseAuth mAuth;
    private static final String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpBtn = findViewById(R.id.signUpBtn);
        usernameSignUp = findViewById(R.id.usernameSignUp);
        emailPengguna = findViewById(R.id.emailPengguna);
        passwordSingUp = findViewById(R.id.passwordSingUp);
        nimPengguna = findViewById(R.id.nimPengguna);

        signUpBtn.setOnClickListener(view -> {
            String username, email, password, NIM;

            username = String.valueOf(usernameSignUp.getText());
            email = String.valueOf(emailPengguna.getText());
            password = String.valueOf(passwordSingUp.getText());
            NIM = String.valueOf(nimPengguna.getText());

            if (TextUtils.isEmpty(username)){
                Toast.makeText(MainActivity2.this, "Enter Username", Toast.LENGTH_LONG).show();
                usernameSignUp.requestFocus();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(MainActivity2.this, "Enter email", Toast.LENGTH_LONG).show();
                emailPengguna.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity2.this, "Enter Password", Toast.LENGTH_LONG).show();
                passwordSingUp.requestFocus();
            } else if (TextUtils.isEmpty(NIM)) {
                Toast.makeText(MainActivity2.this,"Please Insert your NIM", Toast.LENGTH_LONG).show();
                nimPengguna.requestFocus();
            } else {
                //methods public void
                registerUser(username, email, password, NIM);
            }
        });
    }

    private void registerUser(String username, String email,String password, String NIM) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity2.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser fUser = auth.getCurrentUser();
                String uid = fUser.getUid();

                UserDetails userDetails = new UserDetails(uid, username, email, password, NIM);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(fUser.getUid()).setValue(userDetails).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        fUser.sendEmailVerification();
                        Toast.makeText(MainActivity2.this, "Account created", Toast.LENGTH_LONG).show();

                        //Pindah Page
                        Intent intent = new Intent(MainActivity2.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity2.this, "Account registerd failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Register: Error");
                    }
                });
            }
        });
    }
}