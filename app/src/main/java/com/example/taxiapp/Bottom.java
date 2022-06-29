package com.example.taxiapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Bottom extends Fragment {

    SharedPreferences sharedPreferences;
    Place[] directionPoints = new Place[2];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);

        // initialize sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        Button whereTo = view.findViewById(R.id.whereTo);

        //------------------------------------------------------------------------------------------------
        //Activity launcher object: this object allows to launch the google autocomplete intent
        // to input pickup and destination address and receive the
        //results from google places API
        //------------------------------------------------------------------------------------------------
        ActivityResultLauncher<Intent> starPickUpResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result != null) {

                    // dialog.setContentView(R.layout.direction_layout);
                    EditText pickup = dialog.findViewById(R.id.pickUp);
                    Place place = Autocomplete.getPlaceFromIntent(result.getData());
                    pickup.setText(place.getAddress());


                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        ActivityResultLauncher<Intent> starDestinationResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result != null) {


                    EditText destination = dialog.findViewById(R.id.destination);
                    Place place = Autocomplete.getPlaceFromIntent(result.getData());
                    // set destination address text
                    destination.setText(place.getAddress());

                    //store the place result object
                    directionPoints[0] = place;

                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        //------------------------------------------------------------------------------------------------
        //DISPLAY Dialog to enter pick up location and destination
        //------------------------------------------------------------------------------------------------
        whereTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.direction_layout);
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                // access direction_layout views
                Button send = (Button) dialog.findViewById(R.id.send);
                EditText pickup = (EditText) dialog.findViewById(R.id.pickUp);
                EditText destination = (EditText) dialog.findViewById(R.id.destination);


                //------------------------------------------------------------
                // Get places from google place API
                //------------------------------------------------------------

                // Initialize place
                Places.initialize(getContext(), getKey());

                // INPUT PICKUP POINT FROM GOOGLE PLACES
                pickup.setFocusable(false);
                pickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //initialize place field list
                        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                        //Create intent
                        Intent autoCompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                        //start activity
                        starPickUpResult.launch(autoCompleteIntent);

                    }
                });


                // INPUT DESTINATION FROM GOOGLE PLACES
                destination.setFocusable(false);
                destination.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //initialize place field list
                        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                        //Create intent
                        Intent autoCompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                        //start activity
                        starDestinationResult.launch(autoCompleteIntent);

                    }
                });


            }
        });


//------------------------------------------------------------------------------------------------
        //DISPLAY THE TOP 3 DESTINATIONS (inside the recyclerview)
//------------------------------------------------------------------------------------------------
        String[] topThreeDestinations = {"TUT arcadia Campus", "Hospital", "Church"};
        RecyclerView myRecyclerView = view.findViewById(R.id.suggestions);

        SuggestionAdapter adapter = new SuggestionAdapter(topThreeDestinations);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myRecyclerView.setAdapter(adapter);

        return view;

    }

    //getting my google API key from local.properties file
    private String getKey() {
        return BuildConfig.MAP_KEY;
    }


}