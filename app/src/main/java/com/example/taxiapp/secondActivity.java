package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.taxiapp.processor.FetchURL;
import com.example.taxiapp.processor.TaskLoadedCallback;
import com.example.taxiapp.userplace.UserPlace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class secondActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    NavigationView navigationView;
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    SharedPreferences placesPreference;
    UserPlace firstPlace = new UserPlace();
    UserPlace secondPlace = new UserPlace();

    // Button getDirection;
    private Polyline currentPolyline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        placesPreference = getSharedPreferences("placePreferences", Context.MODE_PRIVATE);;

      //  placesPreference = getActivity().getPreferences(MODE_PRIVATE);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map2);
       mapFragment.getMapAsync(this);

        // Initialize pickup location to current location
        Gson gson = new Gson();
        String json1 = placesPreference.getString("firstPlace", "");
        UserPlace firstPlace = gson.fromJson(json1, UserPlace.class);

        String json2 = placesPreference.getString("secondPlace", "");
        UserPlace secondPlace = gson.fromJson(json2, UserPlace.class);




        place1 = new MarkerOptions().position(new LatLng(firstPlace.getLatitude(), firstPlace.getLongitude())).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(secondPlace.getLatitude(), secondPlace.getLongitude())).title("Location 2");

        String url =getUrl(place1.getPosition(), place2.getPosition(), "driving");
        new FetchURL(secondActivity.this).execute(url, "driving");


    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getKey();
        return url;
    }

    //getting my google API key from local.properties file
    private String getKey() {
        return BuildConfig.MAP_KEY;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng(place1.getPosition().latitude, place1.getPosition().longitude);
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16.0f));
    }
}