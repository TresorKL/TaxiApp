package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taxiapp.processor.DataProcessor;
import com.example.taxiapp.threads.InputFieldListiner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText userName;

    Button registerBtn;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.name);

        registerBtn = findViewById(R.id.register);


// Style input fields when inputting...
        userName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (!userName.getText().toString().isEmpty()) {
                    userName.setBackground(green);
                } else {
                    userName.setBackground(initial);
                }
                return false;
            }
        });
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (!password.getText().toString().isEmpty()) {
                    password.setBackground(green);
                } else {
                    password.setBackground(initial);
                }
                return false;
            }
        });


        email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (!email.getText().toString().isEmpty()) {
                    email.setBackground(green);
                } else {
                    email.setBackground(initial);
                }
                return false;
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String userNameText = userName.getText().toString();
                registerUser(emailText, passwordText, userNameText);
            }
        });

        // new InputFieldListiner(CreateAccountActivity.this, getApplicationContext(), email, userName, password).run();

    }

    private void registerUser(String email, String password, String userName) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    storeUserDate(email, userName);
                    Toast.makeText(CreateAccountActivity.this, "Account successfully created ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateAccountActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Account creation failed ", Toast.LENGTH_LONG).show();
                }

            }


        });


    }

    public void storeUserDate(String email, String userName) {


        DataProcessor dataProcessor = new DataProcessor();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", userName);
        map.put("email", email);

        String userId = dataProcessor.generateUserId(email);

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).updateChildren(map);


    }

}