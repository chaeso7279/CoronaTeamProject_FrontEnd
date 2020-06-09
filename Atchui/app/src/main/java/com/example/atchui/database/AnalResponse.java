package com.example.atchui.database;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnalResponse {

    @SerializedName("size")
    public int arraySize;

    @SerializedName("list")
    public String strArray;

    public void ConvertToData(ArrayList<AnalData> lstData) throws JSONException {
        if(lstData == null)
            return;
        if(!lstData.isEmpty())
            lstData.clear();

        // 문자열 변환
        // 먼저 객체 별로 String으로 저장
        ArrayList<String> arrString = new ArrayList<String>();
        int iStartObject = 0, iEndObject = 0;
        for(int i = 0; i < arraySize; ++i){
            iStartObject = strArray.indexOf("{", iEndObject);
            iEndObject = strArray.indexOf("}", iStartObject);

            String strTemp = strArray.substring(iStartObject, iEndObject + 1);
            arrString.add(strTemp);
        }

        for(int i = 0; i < arrString.size(); ++i){
            JSONObject jObj = new JSONObject(arrString.get(i));
            AnalData data = new AnalData();

            data.m_analID = jObj.getInt("anal_id");
            data.m_userID = jObj.getString("user_id");
            data.m_cnfID = jObj.getString("cnf_id");
            data.m_userRouteID = jObj.getInt("user_route_id");
            data.m_cnfRouteID = jObj.getInt("cnf_route_id");

            data.m_analTime = jObj.getString("anal_time");
            data.m_IsPast = jObj.getInt("isPast");
            data.m_IsRead = jObj.getInt("isRead");

            data.m_userLatitude = jObj.getDouble("user_latitude");
            data.m_userLongitude = jObj.getDouble("user_longitude");
            data.m_userVisitTime = jObj.getString("user_visitDatetime");

            data.m_cnfLatitude = jObj.getDouble("cnf_latitude");
            data.m_cnfLongitude = jObj.getDouble("cnf_longitude");
            data.m_locationName = jObj.getString("location_name");
            data.m_cnfVisitTime = jObj.getString("cnf_visittime");
            data.m_color = jObj.getInt("color");

            data.m_cnfInfectCase = jObj.getString("infect_case");
            data.m_cnfDate = jObj.getString("cnf_date");
            data.m_cnfProvince = jObj.getString("province");
            data.m_cnfIsoFacility = jObj.getString("isolation_facility");

            lstData.add(data);
        }
    }
}
