package com.example.taxiapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//------------------------------------------------------------------------------------------------
// DISPLAY TOOLBAR (Burger menu)
//------------------------------------------------------------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.myMenu);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


//------------------------------------------------------------------------------------------------
// DISPLAY The first Fragment (which is the MAP)
//------------------------------------------------------------------------------------------------

        MapsFragment mapsFragment = new MapsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.firstLayout, mapsFragment, mapsFragment.getTag())
                .commit();

//------------------------------------------------------------------------------------------------
// DISPLAY The second Fragment
//------------------------------------------------------------------------------------------------
        Bottom bottom = new Bottom();
        FragmentManager managerBtm = getSupportFragmentManager();
        managerBtm.beginTransaction()
                .replace(R.id.secondLayout, bottom, bottom.getTag())
                .commit();
    }


}