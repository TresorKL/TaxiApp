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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        numberInput = findViewById(R.id.userNum);
//        progressbar = findViewById(R.id.progressbar);
//        int oldUserNum = 0;
//        userPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
//
//        oldUserNum = userPreferences.getInt("userNum", 1);

//        if(oldUserNum==672348817){
//            Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//            startActivity(intent);
//        }
        createAccount= findViewById(R.id.createAccount);
        goBtn = findViewById(R.id.goBtn);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                userNum= Integer.parseInt(numberInput.getText().toString());
//
//                SharedPreferences.Editor editor= userPreferences.edit();
//
//                editor.putInt("userNum",userNum);
//                editor.commit();
//
//
//
//
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);

               // phoneNumber = numberInput.getText().toString();

                //sendVerificationCode(phoneNumber);


            }
        });

    }

    public void sendVerificationCode(String phoneNumber) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+27" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                progressbar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    public  void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodBySystem,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithCredential(credential).addOnCompleteListener(WelcomeActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                 Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                 //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
                }else{
                    Toast.makeText(WelcomeActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


}