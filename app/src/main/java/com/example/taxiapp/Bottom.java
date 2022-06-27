package com.example.taxiapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Recycler;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;


public class Bottom extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_bottom, container, false);

        //------------------------------------------------------------------------------------------------
        //DISPLAY Dialog to enter pick up location and destination
        //------------------------------------------------------------------------------------------------
        Button whereTo = view.findViewById(R.id.whereTo);

        whereTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"WHERE TO GO",Toast.LENGTH_SHORT).show();
                 final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.direction_layout);
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);




            }
        });
//------------------------------------------------------------------------------------------------
        //DISPLAY THE TOP 3 DESTINATIONS (inside the recyclerview)
//------------------------------------------------------------------------------------------------
        String [] topThreeDestinations = {"TUT arcadia Campus", "Hospital", "Church"};
        RecyclerView myRecyclerView = view.findViewById(R.id.suggestions);

        SuggestionAdapter adapter = new SuggestionAdapter(topThreeDestinations);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myRecyclerView.setAdapter(adapter);

        return view;

    }






}