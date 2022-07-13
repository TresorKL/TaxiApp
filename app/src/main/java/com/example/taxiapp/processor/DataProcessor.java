package com.example.taxiapp.processor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taxiapp.CreateAccountActivity;
import com.example.taxiapp.WelcomeActivity;
import com.example.taxiapp.userplace.UserPlace;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class DataProcessor implements DataProcessorInterface {

    Location currentLocation;
    SharedPreferences placesPreference;
    SupportMapFragment supportMapFragment;
    Context context;
    FirebaseAuth auth;

    // this constructor is used in CreateAccountActivity
    public DataProcessor(Context context, FirebaseAuth auth) {
        this.context = context;
        this.auth = auth;

    }

    // This constructor is used in MapsFragment class
    public DataProcessor(Context context,Location currentLocation, SharedPreferences placesPreference, SupportMapFragment supportMapFragment) {

        this.context=context;
        this.currentLocation=currentLocation;
        this.placesPreference=placesPreference;
        this.supportMapFragment=supportMapFragment;
    }


    // extract dot from email address
    public String generateUserId(String email) {

        String userId = "o";
        String[] emailToken = new String[2];
        emailToken = email.split(".com");


        userId = emailToken[0];

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

    //--------------------------------------------------------------------------------------
    // This method helps to store the information of the user who is creating an account
    //--------------------------------------------------------------------------------------

    private void storeUserDate(String email, String userName) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", userName);
        map.put("email", email);

        String userId = generateUserId(email);

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).updateChildren(map);


    }


    //--------------------------------------------------------------------------------------
    // This method allows us to get current location of user
    //--------------------------------------------------------------------------------------
    public void getCurrentLocation(FusedLocationProviderClient fusedLocationProviderClient, UserPlace firstPlace) {

        // initialize places preferences
        placesPreference = context.getSharedPreferences("placePreferences", Context.MODE_PRIVATE);


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();

            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    //  if(location!=null){

                    currentLocation = location;

                    //----------------------------------------------
                    //Store current location inside share preferences
                    // We store it as object callled FirstPlace
                    //----------------------------------------------
                    firstPlace.setLatitude(currentLocation.getLatitude());
                    firstPlace.setLongitude(currentLocation.getLongitude());

                    Address address = null;
                    Geocoder geocoder = new Geocoder(context);
                    try {
                        List<Address> addressList = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                        address = addressList.get(0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    firstPlace.setAddress(address.getLocality());

                    // we use Gson to store objects inside a sharedpreference
                    SharedPreferences.Editor editor = placesPreference.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(firstPlace);
                    editor.putString("firstPlace", json);
                    editor.commit();


                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {


                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            googleMap.addMarker(new MarkerOptions().position(latLng).title("you are here"));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                        }
                    });

                }

            });

        } else {
            requestPermission();

        }

    }


    public Boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }


}
