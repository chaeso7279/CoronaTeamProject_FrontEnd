package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class PatientRouteResponse {
    @SerializedName("cnf_route_id")
    public int m_cnfRouteID;

    @SerializedName("cnf_id")
    public String m_cnfID;

    @SerializedName("visit_datetime")
    public String m_visitDatetime;

    @SerializedName("location_name")
    public String m_locationName;

    @SerializedName("address")
    public String m_address;

    @SerializedName("latitude")
    public double m_latitude;

    @SerializedName("longitude")
    public double m_longitude;
}
