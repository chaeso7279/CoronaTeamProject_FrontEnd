package com.example.atchui.network;

import com.example.atchui.database.AnalData;
import com.example.atchui.database.AnalResponse;
import com.example.atchui.database.GeneralResponse;
import com.example.atchui.database.PatientRouteData;
import com.example.atchui.database.PatientRouteResponse;
import com.example.atchui.database.SettingData;
import com.example.atchui.database.UserPresentData;
import com.example.atchui.database.UserRouteData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceAPI {

    //js코드의 POST에서 사용 - 아래 함수를 실행하면 이 url로 데이터 전송
    @POST("/user/option")
    Call<GeneralResponse>  userOption(@Body SettingData data);
    //GeneralResponse : 클라이언트가 정보를 받을 데이터 형식 정해준 것
    //@Body:  json으로 바꿔 줌
    //SettingData data: 서버에 보낼 데이터 형태 정함

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

    @POST("/analysis/GetAnalData")
    Call<AnalResponse> GetAnalList(@Body AnalData data);

    @POST("/analysis/Present")
    Call <GeneralResponse> AnalPresentRoute(@Body UserPresentData data);

    @POST("/analysis/Past")
    Call <GeneralResponse> AnalPastRoute(@Body AnalData data);

    @POST("/analysis/UpdateIsRead")
    Call<GeneralResponse> UpdateAnalIsRead(@Body AnalData data);
}