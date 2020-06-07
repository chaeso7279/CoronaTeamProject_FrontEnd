package com.example.atchui.network;

import com.example.atchui.database.AnalData;
import com.example.atchui.database.AnalResponse;
import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.PatientRouteResponse;
import com.example.atchui.database.SettingData;
import com.example.atchui.database.SettingResponse;
import com.example.atchui.database.UserRouteData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceAPI {

    @POST("/user/option")
    Call<SettingResponse>  userOption(@Body SettingData data);

    @POST("/user/option/updateRad")
    Call<SettingData>  userOptionUpdateRad(@Body SettingData data);

    @POST("/user/option/updatePeriod")
    Call<SettingData>  userOptionUpdatePeriod(@Body SettingData data);

    @POST("/user/getOption")
    Call<SettingData> GetUserOption(@Body SettingData data);

    @POST("/cnf_patient/route")
    Call<PatientRouteResponse> cnfPatientRoute(@Body PatientRouteData data);

    @POST("/user/SendRoute")
    Call<UserRouteData> SendUserRoute(@Body UserRouteData data);

    @POST("/analysis")
    Call<AnalResponse> GetAnalList(@Body AnalData data);

    @POST("/AnalRoute/Present")
    Call <AnalResponse> AnalPresentRoute(@Body AnalData data);

    @POST("/analysis/UpdateIsRead")
    Call<SettingResponse> UpdateAnalIsRead(@Body AnalData data);
}