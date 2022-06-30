package com.example.taxiapp;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taxiapp.userplace.UserPlace;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.Gson;

import java.util.List;

public class MapsFragment extends Fragment {
    private Location currentLocation;
    LocationRequest mLocationRequest;
    UserPlace firstPlace = new UserPlace();
    SupportMapFragment supportMapFragment;

   // SharedPreferences placesPreference = getActivity().getPreferences(MODE_PRIVATE);
    SharedPreferences placesPreference;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    public Location getCurrentL() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       // placesPreference = getActivity().getSharedPreferences("MyUserPrefs", getContext().MODE_PRIVATE);
        getCurrentLocation();

        return view;
    }


    private void getCurrentLocation() {

        // initialize places preferences
        placesPreference = getActivity().getSharedPreferences("placePreferences", Context.MODE_PRIVATE);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();

            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    //  if(location!=null){

                    currentLocation = location;

                    //----------------------------------------------
                    //Store current location inside share preferences
                    //----------------------------------------------
                    firstPlace.setLatitude(currentLocation.getLatitude());
                    firstPlace.setLongitude(currentLocation.getLongitude());

                    Address address=null;
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        List<Address> addressList=geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                        address=addressList.get(0);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    firstPlace.setAddress(address.getLocality());

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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

}