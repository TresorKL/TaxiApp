package com.example.taxiapp.threads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.res.ResourcesCompat;

import com.example.taxiapp.CreateAccountActivity;
import com.example.taxiapp.R;

public class InputFieldListiner extends Thread {
    EditText email, userName, password;
    Context context;
    Button registerBtn;
    Activity activity;

    public InputFieldListiner( EditText email,EditText userName,EditText password, Button registerBtn) {



        this.registerBtn = registerBtn;
        this.email=email;
        this.userName=userName;
        this.password=password;


    }

    public void styleRegisterBtn() {
        Drawable green = ResourcesCompat.getDrawable(context.getResources(), R.drawable.style_input_field, null);
        Drawable initial = ResourcesCompat.getDrawable(context.getResources(), R.drawable.input_fields, null);




    }


    public void run() {


        // Activity activity =(Activity) context;
        while (true) {



        }
    }


}
