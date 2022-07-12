package com.example.taxiapp.threads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.EditText;

import androidx.core.content.res.ResourcesCompat;

import com.example.taxiapp.CreateAccountActivity;
import com.example.taxiapp.R;

public class InputFieldListiner extends Thread{
    EditText email, userName, password;
    Context context;
    Activity activity;

    public InputFieldListiner(Activity activity, Context context,EditText email,EditText userName,EditText password ){

        this.context=context;
        this.email=email;
        this.password=password;
        this.userName=userName;
        this.activity=activity;




    }

    public void styleInputs(){
        Drawable green = ResourcesCompat.getDrawable(context.getResources(), R.drawable.style_input_field, null);
        Drawable initial = ResourcesCompat.getDrawable(context.getResources(), R.drawable.input_fields, null);


    if (!userName.getText().toString().isEmpty()) {
        userName.setBackground(green);
    } else{
        userName.setBackground(initial);
    }

    if (!email.getText().toString().isEmpty()) {
        email.setBackground(green);
    } else  {
        email.setBackground(initial);
    }

    if (!password.getText().toString().isEmpty()) {
        password.setBackground(green);
    } else {
        password.setBackground(initial);
    }

    }


    public void run(){


      // Activity activity =(Activity) context;
       while(true) {

             styleInputs();

         }
    }



}
