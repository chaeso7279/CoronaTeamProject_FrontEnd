package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class UserRouteData {

    @SerializedName("user_id")
    public String m_userID;

    @SerializedName("user_route_id")
    public int m_userRouteID;

    @SerializedName("user_datetime")
    public String m_userDateTime;

    @SerializedName("latitude")
    public double m_latitude;

    @SerializedName("longitude")
    public double m_longitude;
}
