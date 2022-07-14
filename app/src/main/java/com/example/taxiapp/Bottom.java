package com.example.taxiapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import com.example.taxiapp.userplace.UserPlace;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Bottom extends Fragment {

    SharedPreferences placesPreference;
    Location startPoint = new Location("location 1");
    Location endPoint= new Location("location 2");
    UserPlace firstPlace = new UserPlace();
    UserPlace secondPlace = new UserPlace();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);

        // initialize sharedPreferences;

        //placesPreference = getActivity().getPreferences(MODE_PRIVATE);
        placesPreference = getActivity().getSharedPreferences("placePreferences", Context.MODE_PRIVATE);
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

                    if (result.getResultCode() != 0) {
                        EditText pickup = dialog.findViewById(R.id.pickUp);
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());

                        pickup.setText(place.getAddress());

                        //----------------------------------------------
                        //Store first place inside share preferences
                        //----------------------------------------------
                        firstPlace.setLatitude(place.getLatLng().latitude);
                        firstPlace.setLongitude(place.getLatLng().longitude);
                        firstPlace.setAddress(place.getAddress());
                        SharedPreferences.Editor editor = placesPreference.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(firstPlace);
                        editor.putString("firstPlace", json);
                        editor.commit();


                        // Setting location so we can be able to determine the distance between 2 locations
                        startPoint.setLatitude(place.getLatLng().latitude);
                        startPoint.setLongitude(place.getLatLng().longitude);


                    }


                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(result.getData());
                    Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        ActivityResultLauncher<Intent> starDestinationResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Place place = null;
                if (result != null) {
                    if (result.getResultCode() != 0) {

                        EditText destination = dialog.findViewById(R.id.destination);
                        place = Autocomplete.getPlaceFromIntent(result.getData());
                        destination.setText(place.getAddress());

                        //----------------------------------------------
                        //Store first place inside share preferences
                        //----------------------------------------------
                        secondPlace.setLatitude(place.getLatLng().latitude);
                        secondPlace.setLongitude(place.getLatLng().longitude);
                        secondPlace.setAddress(place.getAddress());
                        SharedPreferences.Editor editor = placesPreference.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(secondPlace);
                        editor.putString("secondPlace", json);
                        editor.commit();



                        // Setting location so we can be able to determine the distance between 2 locations
                        endPoint.setLatitude(place.getLatLng().latitude);
                        endPoint.setLongitude(place.getLatLng().longitude);

                    }

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

                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.direction_layout);
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                // access direction_layout views
                Button proceedTrip = (Button) dialog.findViewById(R.id.proceedTrip);
                EditText pickup = (EditText) dialog.findViewById(R.id.pickUp);
                EditText destination = (EditText) dialog.findViewById(R.id.destination);

                // Initialize pickup location to current location
                Gson gson = new Gson();
                String json = placesPreference.getString("firstPlace", "");
                UserPlace firstPlace = gson.fromJson(json, UserPlace.class);
                startPoint.setLatitude(firstPlace.getLatitude());
                startPoint.setLongitude(firstPlace.getLongitude());
                pickup.setText(firstPlace.getAddress());

                proceedTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(firstPlace.getAddress()!=null&&secondPlace.getAddress()!=null){

                        // getting distance of the trip in meters
                        double distanceInMeter = startPoint.distanceTo(endPoint);

                       double distanceInKm = distanceInMeter * 0.001;

                        if(distanceInKm <45) {

                            int distanceNet=(int)distanceInKm;

                             Intent nextIntent = new Intent(getContext(), secondActivity.class);
                             nextIntent.putExtra("distance", distanceNet);
                             startActivity(nextIntent);
                             Toast.makeText(getContext(),"DISTANCE: " +distanceNet +" Km",Toast.LENGTH_SHORT).show();
                         }else{
                             Toast.makeText(getContext(),"INVALID TRIP THE DISTANCE IS TOO LONG "+distanceInKm+"Km",Toast.LENGTH_SHORT).show();
                         }
                        }else{
                            Toast.makeText(getContext(),"PLEASE SPECIFY ALL THE LOCATIONS",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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