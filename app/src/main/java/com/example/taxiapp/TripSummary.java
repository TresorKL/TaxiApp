package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxiapp.processor.DataProcessor;
import com.example.taxiapp.processor.FetchURL;
import com.example.taxiapp.processor.TaskLoadedCallback;
import com.example.taxiapp.userplace.UserPlace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TripSummary extends AppCompatActivity  implements  TaskLoadedCallback {
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private GoogleMap mMap;
    SharedPreferences placesPreference;
    SupportMapFragment supportMapFragment;
    DataProcessor dataProcessor = new DataProcessor(TripSummary.this);
    TextView date, price, from,to, distance;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);

       // supportMapFragment = (SupportMapFragment)findFragmentById(R.id.map);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.mapSummary)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng latLng = new LatLng(place1.getPosition().latitude, place1.getPosition().longitude);
                mMap = googleMap;
                Log.d("mylog", "Added Markers");
                mMap.addMarker(place1);
                mMap.addMarker(place2);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
            }
        });



        placesPreference = getSharedPreferences("placePreferences", Context.MODE_PRIVATE);;

       // get places
        Gson gson = new Gson();
        String json1 = placesPreference.getString("firstPlace", "");
        UserPlace firstPlace = gson.fromJson(json1, UserPlace.class);

        String json2 = placesPreference.getString("secondPlace", "");
        UserPlace secondPlace = gson.fromJson(json2, UserPlace.class);

        // get distance
        Bundle extras = getIntent().getExtras();
        int distanceNet = (int) extras.get("distance");

        //get price
        String amountDueStr = (String) extras.get("price");

        int amountDue= Integer.parseInt(amountDueStr.substring(1));

        //get current date
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);


        // get views
        date = findViewById(R.id.date);
        from= findViewById(R.id.from);
        to= findViewById(R.id.to);
        distance=  findViewById(R.id.distance);
        price=  findViewById(R.id.amount);
        confirm =findViewById(R.id.confirm);

        date.setText(formattedDate);
        from.setText(from.getText().toString()+firstPlace.getAddress());
        to.setText(to.getText().toString()+secondPlace.getAddress());
        distance.setText(distance.getText().toString()+distanceNet+" Km");
        price.setText(price.getText().toString()+amountDue);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataProcessor.StoreTrip( firstPlace,  secondPlace, distanceNet ,  formattedDate,  amountDue);

                Toast.makeText(TripSummary.this,"Trip saved",Toast.LENGTH_LONG).show();
            }
        });






        place1 = new MarkerOptions().position(new LatLng(firstPlace.getLatitude(), firstPlace.getLongitude())).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(secondPlace.getLatitude(), secondPlace.getLongitude())).title("Location 2");
        String url =getUrl(place1.getPosition(), place2.getPosition(), "driving");
        new FetchURL(TripSummary.this).execute(url, "driving");
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

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        LatLng latLng = new LatLng(place1.getPosition().latitude, place1.getPosition().longitude);
//        mMap = googleMap;
//        Log.d("mylog", "Added Markers");
//        mMap.addMarker(place1);
//        mMap.addMarker(place2);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16.0f));
//    }
}