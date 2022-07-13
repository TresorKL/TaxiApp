package com.example.taxiapp.processor;

public interface DataProcessorInterface {
    public String generateUserId(String email);
    public void registerUser(String email, String password, String userName);
}
