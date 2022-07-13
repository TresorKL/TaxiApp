package com.example.taxiapp.processor;

import com.example.taxiapp.userplace.UserPlace;
import com.google.android.gms.location.FusedLocationProviderClient;

public interface DataProcessorInterface {
    public String generateUserId(String email);
    public void registerUser(String email, String password, String userName);
    public void getCurrentLocation(FusedLocationProviderClient fusedLocationProviderClient, UserPlace firstPlace);
}
