package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class AnalData {
    @SerializedName("anal_id")
    public int m_analID;

    @SerializedName("user_id")
    public String m_userID;

    @SerializedName("cnf_id")
    public String m_cnfID;

    @SerializedName("user_route_id")
    public int m_userRouteID;

    @SerializedName("cnf_route_id")
    public int m_cnfRouteID;

    @SerializedName("user_latitude")
    public double m_userLatitude;

    @SerializedName("user_longitude")
    public double m_userLongitude;

    @SerializedName("cnf_latitude")
    public double m_cnfLatitude;

    @SerializedName("cnf_longitude")
    public double m_cnfLongitude;

    @SerializedName("location_name")
    public String m_locationName;

    @SerializedName("color")
    public int m_color;

    @SerializedName("anal_time")
    public String m_analTime;

    @SerializedName("isPast")
    public int m_IsPast;

    @SerializedName("isRead")
    public int m_IsRead;
}
