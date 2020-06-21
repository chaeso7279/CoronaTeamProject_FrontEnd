package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class UserPresentData {

    @SerializedName("user_id")
    public String userID;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    public UserPresentData() {}

    public UserPresentData(String userID, double latitude, double longitude){
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}