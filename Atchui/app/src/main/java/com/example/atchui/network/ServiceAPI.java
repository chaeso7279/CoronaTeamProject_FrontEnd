package com.example.atchui.network;

import com.example.atchui.database.SettingData;
import com.example.atchui.database.SettingResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceAPI {

    @POST("/user/option")
    Call<SettingResponse>  userOption(@Body SettingData data);
}