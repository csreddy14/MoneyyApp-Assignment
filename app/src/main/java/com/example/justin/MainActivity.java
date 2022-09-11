package com.example.justin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    String loggedEmail;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);

        String welcomeMessage = String.format("Hi %s!", getIntent().getStringExtra("email"));
        TextView tvEmail = (TextView) findViewById(R.id.tv_welcome);
        tvEmail.setText(welcomeMessage);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logout Successful !", Toast.LENGTH_SHORT).show();

            }
        });

        if (mAuth.getCurrentUser() != null) {
            loggedEmail = mAuth.getCurrentUser().getEmail();
        } else {
            Toast.makeText(MainActivity.this, "Error = No users Found !", Toast.LENGTH_SHORT).show();
        }

    }
}