package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


        String emailPreference;
        userPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);

       
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


                String email = emailAccess.getText().toString();
                String password = passwordAccess.getText().toString();
                loginUser(auth, email, password);



            }


        });

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
                    Toast.makeText(WelcomeActivity.this, "INVALID LOGIN", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


}