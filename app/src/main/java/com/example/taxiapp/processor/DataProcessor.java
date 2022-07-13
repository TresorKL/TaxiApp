package com.example.taxiapp.processor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.taxiapp.CreateAccountActivity;
import com.example.taxiapp.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DataProcessor implements DataProcessorInterface {

    Context context;
    FirebaseAuth auth;
    public DataProcessor(Context context,   FirebaseAuth auth) {
        this.context=context;
        this.auth=auth;
    }

    // extract dot from email address
    public String generateUserId(String email){

        String userId="o";
        String[] emailToken = new String[2];
        emailToken = email.split(".com");


        userId= emailToken[0];

        return userId;
    }

    public void registerUser(String email, String password, String userName) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    storeUserDate(email, userName);
                    Toast.makeText(context, "Account successfully created ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Account creation failed ", Toast.LENGTH_LONG).show();
                }

            }


        });


    }

    private void storeUserDate(String email, String userName) {




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", userName);
        map.put("email", email);

        String userId = generateUserId(email);

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).updateChildren(map);


    }
}
