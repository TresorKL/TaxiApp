package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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




        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText= password.getText().toString();
                String userNameText= userName.getText().toString();
                registerUser( emailText,   passwordText,  userNameText);
            }
        });


    }

    private void registerUser(String email, String password, String userName) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(CreateAccountActivity.this,"Account successfully created ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateAccountActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(CreateAccountActivity.this,"Account creation failed ",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}