package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button registerBtn;
    DataProcessor dataProcessor = new DataProcessor(CreateAccountActivity.this, auth);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.name);


        registerBtn = findViewById(R.id.register);
        

        // Style input fields when inputting...


        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int before, int after, int counter) {

                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (s.toString().trim().length() > 0) {
                    userName.setBackground(green);


                } else {
                    userName.setBackground(initial);
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (s.toString().trim().length() > 0) {
                    userName.setBackground(green);

                } else {

                    userName.setBackground(initial);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int before, int after, int counter) {

                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (s.toString().trim().length() > 0) {
                    password.setBackground(green);

                } else {

                    password.setBackground(initial);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int before, int after, int counter) {
                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (s.toString().trim().length() > 0) {
                    password.setBackground(green);

                } else {

                    password.setBackground(initial);
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int before, int after, int counter) {

                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (s.toString().trim().length() > 0) {
                    email.setBackground(green);

                } else {

                    email.setBackground(initial);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                Drawable green = ResourcesCompat.getDrawable(getResources(), R.drawable.style_input_field, null);
                Drawable initial = ResourcesCompat.getDrawable(getResources(), R.drawable.input_fields, null);
                if (s.toString().trim().length() > 0) {
                    email.setBackground(green);

                } else {

                    email.setBackground(initial);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable warningStyle = ResourcesCompat.getDrawable(getResources(), R.drawable.style_warning_input, null);

                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String userNameText = userName.getText().toString();


                if (!emailText.isEmpty() && !passwordText.isEmpty() && !userNameText.isEmpty() && emailText.contains(".com")) {
                    // register user
                    dataProcessor.registerUser(emailText, passwordText, userNameText);
                } else if (emailText.isEmpty()) {
                    email.setBackground(warningStyle);
                } else if (passwordText.isEmpty()) {
                    password.setBackground(warningStyle);
                } else if (userNameText.isEmpty()) {
                    userName.setBackground(warningStyle);
                } else {
                    email.setBackground(warningStyle);
                }
            }
        });

    }


}