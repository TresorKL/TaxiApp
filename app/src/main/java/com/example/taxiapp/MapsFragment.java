package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
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

import java.util.List;

public class MapsFragment extends Fragment {
    private Location currentLocation;
    LocationRequest mLocationRequest;
    SupportMapFragment supportMapFragment;

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

        getCurrentLocation();

        return view;
    }


//    OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            getCurrentLocation();
//            LatLng latLng = new LatLng(25.75852,28.20515);
//            googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
//            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//
//
//        }
//
//
//    };


    private void getCurrentLocation() {


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();

            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    //  if(location!=null){

                    currentLocation = location;

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            getCurrentLocation();

                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            googleMap.addMarker(new MarkerOptions().position(latLng).title("you are here"));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                        }
                    });

                    //https://www.google.com/maps/place/Googleplex,+1600+Amphitheatre+Pkwy,+Mountain+View,+CA+94043,+United+States/@37.4219983,-122.084,17z/data=!4m2!3m1!1s0x808fba02425dad8f:0x6c296c66619367e0?source=mlgmmupgrade


//                     Toast.makeText(getContext()," "+getCurrentL().getLongitude(),Toast.LENGTH_LONG).show();
//                           Address address=null;
//                           Geocoder geocoder = new Geocoder(getContext());
//                           try {
//
//                                List<Address> addressList=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                             //  List<Address> addressList=geocoder.getFromLocation(-25.758062186313445,28.204679632844414, 1);
//                               address=addressList.get(0);
//                          Toast.makeText(getContext(),address.getCountryName()+":  "+address.getLocality(),Toast.LENGTH_LONG).show();
//                           }catch (Exception ex){
//                               ex.printStackTrace();
//                           }

                    //  Toast.makeText(getContext(),address.getCountryName()+":  "+address.getLocality(),Toast.LENGTH_LONG).show();
                    //}
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