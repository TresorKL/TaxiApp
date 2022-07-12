package com.example.taxiapp.processor;

public class DataProcessor {

    public DataProcessor() {
    }

    // extract dot from email address
    public String generateUserId(String email){

        String userId="o";
        String[] emailToken = new String[2];
        emailToken = email.split(".com");


        userId= emailToken[0];



        return userId;
    }
}
