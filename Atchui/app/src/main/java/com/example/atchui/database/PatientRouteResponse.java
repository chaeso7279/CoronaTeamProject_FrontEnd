package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientRouteResponse {
    @SerializedName("size")
    public int arraySize;

    @SerializedName("list")
    public String strArray;

    public void ConvertToData(ArrayList<PatientRouteData> lstData) throws JSONException {
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
            PatientRouteData data = new PatientRouteData();

            data.m_cnfRouteID = jObj.getInt("cnf_route_id");
            data.m_cnfID = jObj.getString("cnf_id");
            data.m_visitDatetime = jObj.getString("visit_datetime");
            data.m_locationName = jObj.getString("location_name");
            data.m_address = jObj.getString("address");
            data.m_latitude = jObj.getDouble("latitude");
            data.m_longitude = jObj.getDouble("longitude");
            data.m_color = jObj.getInt("color");

            lstData.add(data);
        }
    }
}
