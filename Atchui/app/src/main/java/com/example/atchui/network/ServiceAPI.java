package com.example.atchui.network;

import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.PatientRouteResponse;
import com.example.atchui.database.SettingData;
import com.example.atchui.database.SettingResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceAPI {

    @POST("/user/option")
    Call<SettingResponse>  userOption(@Body SettingData data);

    @POST("/user/option/updateRad")
    Call<SettingResponse>  userOptionUpdateRad(@Body SettingData data);

    @POST("/user/option/updatePeriod")
    Call<SettingResponse>  userOptionUpdatePeriod(@Body SettingData data);

    @POST("/cnf_patient/route")
    Call<PatientRouteResponse> cnfPatientRoute(@Body PatientRouteData data);
}