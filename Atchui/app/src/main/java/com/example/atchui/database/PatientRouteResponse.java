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
        // 서버에서 보낼 때 배열을 string으로 바꿔서 보내줬음
        // 그걸 객체 별로 나눠서 하나씩 strArray배열에 저장
        ArrayList<String> arrString = new ArrayList<String>();
        int iStartObject = 0, iEndObject = 0;
        for(int i = 0; i < arraySize; ++i){
            iStartObject = strArray.indexOf("{", iEndObject);
            iEndObject = strArray.indexOf("}", iStartObject);

            String strTemp = strArray.substring(iStartObject, iEndObject + 1);
            arrString.add(strTemp);
        }


        for(int i = 0; i < arrString.size(); ++i){
            JSONObject jObj = new JSONObject(arrString.get(i)); //객체 하나씩 다시 서버에서의 json형태로 바꿔줌

            PatientRouteData data = new PatientRouteData();

            //json의 데이터를 data에 저장
            data.m_cnfRouteID = jObj.getInt("cnf_route_id");
            data.m_cnfID = jObj.getString("cnf_id");
            data.m_visitDatetime = jObj.getString("visit_datetime");
            data.m_locationName = jObj.getString("location_name");
            data.m_address = jObj.getString("address");
            data.m_latitude = jObj.getDouble("latitude");
            data.m_longitude = jObj.getDouble("longitude");
            data.m_color = jObj.getInt("color");

            //저장된 data를 리스트에 add
            lstData.add(data);
        }
    }
}
