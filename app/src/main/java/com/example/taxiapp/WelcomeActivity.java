package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class WelcomeActivity extends AppCompatActivity {
    String verificationCodBySystem;
    Button goBtn;
    EditText numberInput;
    SharedPreferences userPreferences;
    String phoneNumber;
    TextView createAccount;
    ProgressBar progressbar;
    EditText emailAccess, passwordAccess;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        requestPermission();


        String emailPreference;
        userPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);

        //-------------------------------------
        //check if user has logged in before and give him/her access to the system
        // without asking them login again
        //-------------------------------------
        emailPreference = userPreferences.getString("email", "");
        if (!emailPreference.isEmpty()) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        auth = FirebaseAuth.getInstance();

        createAccount = findViewById(R.id.createAccount);
        goBtn = findViewById(R.id.goBtn);

        passwordAccess = findViewById(R.id.passwordAccess);
        emailAccess = findViewById(R.id.emailAccess);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable warningStyle = ResourcesCompat.getDrawable(getResources(), R.drawable.style_warning_input, null);

                String email = emailAccess.getText().toString();
                String password = passwordAccess.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser(auth, email, password);

                } else if (email.isEmpty()) {
                    emailAccess.setBackground(warningStyle);


                } else if (password.isEmpty()) {
                    passwordAccess.setBackground(warningStyle);

                } else {

                    emailAccess.setBackground(warningStyle);
                    passwordAccess.setBackground(warningStyle);

                }


            }

        });


    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);

    }


    private void loginUser(FirebaseAuth auth, String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    SharedPreferences.Editor editor = userPreferences.edit();
                    editor.putString("email", email);
                    editor.commit();


                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(WelcomeActivity.this, "INCORRECT PASSWORD OR EMAIL ADDRESS", Toast.LENGTH_LONG).show();


                }
            }
        });

    }


}