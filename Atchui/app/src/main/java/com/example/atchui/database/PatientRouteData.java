package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class PatientRouteData {
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

    public PatientRouteData() {}
    public PatientRouteData(int cnfRouteID, String cnfID, String visitDateTime, String locationName, String address, double latitude, double longitude)
    {
        this.m_cnfRouteID = cnfRouteID;
        this.m_cnfID = cnfID;
        this.m_visitDatetime = visitDateTime;
        this.m_locationName = locationName;
        this.m_address = address;
        this.m_latitude = latitude;
        this.m_longitude = longitude;

    }
}
